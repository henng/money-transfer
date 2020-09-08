package com.antgroup.moneytransfer.mq;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.antgroup.moneytransfer.dao.TransferReceiveDao;
import com.antgroup.moneytransfer.model.TransferSession;
import com.antgroup.moneytransfer.service.UserService;

/**
 * MessagePuller
 *
 * @author henng
 * @since 2020/9/7
 */
public class MessagePuller {
    @Autowired
    private UserService userService;
    @Autowired
    private TransferReceiveDao transferReceiveDao;

    private final String cardNo;

    private final MessageQueue messageQueue;

    private static final ScheduledExecutorService puller = Executors.newScheduledThreadPool(1);

    public MessagePuller(String cardNo, MessageQueue messageQueue) {
        this.cardNo = cardNo;
        this.messageQueue = messageQueue;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransferReceiveDao(TransferReceiveDao transferReceiveDao) {
        this.transferReceiveDao = transferReceiveDao;
    }

    public void start() {
        MessagePulledChecker messagePulledChecker = new MessagePulledChecker();

        puller.scheduleWithFixedDelay(messagePulledChecker, 1, 1, TimeUnit.SECONDS);
    }

    private class MessagePulledChecker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                TransferSession transferSession = messageQueue.pull();
                if (null == transferSession) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }

                if (!cardNo.equals(transferSession.getTransferTo())) {
                    continue;
                }

                // 基本消费去重
                TransferSession old = transferReceiveDao.get(transferSession.getTransferNo());
                if (old != null) {
                    continue;
                }

                localReceiveTxn(transferSession);

                messageQueue.ack(transferSession);
            }

        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void localReceiveTxn(TransferSession transferSession) {
        userService.transferIn(cardNo, transferSession.getAmount());

        transferReceiveDao.insert(transferSession);
    }
}
