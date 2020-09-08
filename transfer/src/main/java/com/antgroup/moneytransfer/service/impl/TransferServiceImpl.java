package com.antgroup.moneytransfer.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antgroup.moneytransfer.dao.TransferDao;
import com.antgroup.moneytransfer.dao.TransferReceiveDao;
import com.antgroup.moneytransfer.model.ErrorMsg;
import com.antgroup.moneytransfer.model.TransferRequest;
import com.antgroup.moneytransfer.model.TransferResponse;
import com.antgroup.moneytransfer.model.TransferSession;
import com.antgroup.moneytransfer.mq.MessagePuller;
import com.antgroup.moneytransfer.mq.MessageQueue;
import com.antgroup.moneytransfer.mq.MessageSender;
import com.antgroup.moneytransfer.service.TransferService;
import com.antgroup.moneytransfer.service.UserService;

/**
 * TransferServiceImpl
 *
 * @author henng
 * @since 2020/9/7
 */
@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

    // 假设转账最高限额为 ¥10000
    private static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("10000.00");
    // 假设转账最低限额为 ¥0.01
    private static final BigDecimal MIN_TRANSFER_AMOUNT = new BigDecimal("0.01");
    // 假设5分钟内的请求有效
    private static final long MAX_DURATION_FOR_REQUEST = 5 * 60 * 1000;
    // 预设几个 token，实际情况可以在登录时生成再下发
    private static final List<String> DEFAULT_TOKENS = Arrays.asList("token1", "token2", "token3");

    @Autowired
    private UserService userService;
    @Autowired
    private TransferDao transferDao;

    private MessageSender sender;
    private MessagePuller puller;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransferDao(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @Override
    public TransferResponse transfer(TransferRequest request) {
        // 接口安全校验
        TransferResponse transferResponse = requestValidationCheck(request);
        if (transferResponse != null) {
            return transferResponse;
        }

        // 参数校验
        transferResponse = requestParamCheck(request);
        if (transferResponse != null) {
            return transferResponse;
        }

        // 幂等校验
        TransferSession session = transferDao.get(request.getTransferNo());
        if (session != null) {
            LOGGER.info("重复交易，忽略");
            return new TransferResponse(true, ErrorMsg.DUPLICATE_TRANSFER);
        }

        // 设置定时消息任务
        setUpMessageQueue(request);

        // 扣减+本地消息表
        localTransferTxn(request);

        return new TransferResponse(true, ErrorMsg.SUCCESS);
    }


    /**
     * 转出账号扣减金额、插入转账消息，在同一个本地事务里
     *
     * @param request 转账请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void localTransferTxn(TransferRequest request) {
        userService.transferOut(request.getTransferFrom(), request.getAmount());

        TransferSession newSession = new TransferSession()
            .setTransferNo(request.getTransferNo())
            .setAmount(request.getAmount())
            .setTransferFrom(request.getTransferFrom())
            .setTransferTo(request.getTransferTo())
            .setTransferStartTime(System.currentTimeMillis());

        transferDao.insert(newSession);
    }

    private TransferResponse requestValidationCheck(TransferRequest request) {
        if (request == null) {
            LOGGER.error("请求参数不能为空");
            return new TransferResponse(false, ErrorMsg.ILLEGAL_PARAM);
        }

        if (System.currentTimeMillis() < request.getTimestamp()
            || System.currentTimeMillis() - request.getTimestamp() > MAX_DURATION_FOR_REQUEST) {
            LOGGER.warn("请求：{} 超过有效时间", request.getTransferNo());
            return new TransferResponse(false, ErrorMsg.ILLEGAL_REQUEST);
        }

        if (!DEFAULT_TOKENS.contains(request.getToken())) {
            LOGGER.warn("请求：{} 所含 token 无效", request.getTransferNo());
            return new TransferResponse(false, ErrorMsg.ILLEGAL_REQUEST);
        }

        return null;
    }

    /**
     * 参数基本校验
     *
     * @param request 转账请求
     *
     * @return null（校验通过）；not null（校验不通过）
     */
    private TransferResponse requestParamCheck(TransferRequest request) {
        if (request == null) {
            LOGGER.error("请求参数不能为空");
            return new TransferResponse(false, ErrorMsg.ILLEGAL_PARAM);
        }

        // 以下校验，可以再根据业务逻辑，添加业务自己的校验

        boolean illegalTransferNo = StringUtils.isEmpty(request.getTransferNo());
        if (illegalTransferNo) {
            LOGGER.error("转账流水号不能为空");
        }

        boolean illegalTransferFrom = StringUtils.isEmpty(request.getTransferFrom());
        if (illegalTransferFrom) {
            LOGGER.error("流水号：{} 的转出账号不能为空", request.getTransferNo());
        }

        boolean illegalTransferTo = StringUtils.isEmpty(request.getTransferTo());
        if (illegalTransferTo) {
            LOGGER.error("流水号：{} 的转入账号不能为空", request.getTransferNo());
        }

        if (illegalTransferNo || illegalTransferFrom || illegalTransferTo) {
            return new TransferResponse(false, ErrorMsg.ILLEGAL_PARAM);
        }

        BigDecimal amount = request.getAmount();
        if (amount == null) {
            LOGGER.error("流水号：{} 的转账不能为空", request.getTransferNo());
            return new TransferResponse(false, ErrorMsg.ILLEGAL_PARAM);
        }

        boolean illegalAmount = false;
        if (amount.compareTo(MIN_TRANSFER_AMOUNT) <= 0) {
            LOGGER.error("流水号：{} 的转账金额不能少于0.01", request.getTransferNo());
            illegalAmount = true;
        }

        // 还可以检查转账限额、小数位等
        if (amount.compareTo(MAX_TRANSFER_AMOUNT) > 0) {
            LOGGER.error("流水号：{} 的转账超过限额", request.getTransferNo());
            illegalAmount = true;
        }
        if (illegalAmount) {
            return new TransferResponse(false, ErrorMsg.ILLEGAL_PARAM);
        }

        return null;
    }

    private void setUpMessageQueue(TransferRequest request) {
        MessageQueue messageQueue = new MessageQueue();
        if (sender == null) {
            sender = new MessageSender(messageQueue);
            sender.setTransferDao(transferDao);
            sender.start();
        }

        if (puller == null) {
            puller = new MessagePuller(request.getTransferTo(), messageQueue);
            puller.setUserService(userService);
            puller.setTransferReceiveDao(new TransferReceiveDao());
            puller.start();
        }
    }
}
