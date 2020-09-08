package com.antgroup.moneytransfer.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.antgroup.moneytransfer.model.ErrorMsg;
import com.antgroup.moneytransfer.model.TransferSession;

/**
 * TransferDao
 *
 * @author henng
 * @since 2020/9/7
 */
@Repository
public class TransferDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferDao.class);

    // 在内存里面，简单的模拟数据库
    private final Map<String, TransferSession> sessionMap = new ConcurrentHashMap<>(16);

    public TransferSession get(String transferNo) {
        return sessionMap.get(transferNo);
    }

    public void insert(TransferSession session) {
        TransferSession old = sessionMap.putIfAbsent(session.getTransferNo(), session);
        if (old != null) {
            LOGGER.warn("交易号：{} 已存在，忽略", session.getTransferNo());
            throw new RuntimeException(ErrorMsg.DUPLICATE_TRANSFER.getMessage());
        }
    }

    public void update(TransferSession session) {
        sessionMap.put(session.getTransferNo(), session);
    }

    /**
     * 取出一部分未处理的，实际情况可能类似是:
     * select * from transfer_session where has_sent_mq = false order by start_timestamp limit 10
     * 这里只简单返回全部数据做演示
     *
     * @return 记录集合
     */
    public Collection<TransferSession> getUnsentMessages() {
        return sessionMap.values();
    }
}
