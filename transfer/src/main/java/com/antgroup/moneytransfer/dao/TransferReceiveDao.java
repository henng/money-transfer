package com.antgroup.moneytransfer.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.antgroup.moneytransfer.model.TransferSession;

/**
 * TransferReceiveDao
 * 消费记录
 *
 * @author henng
 * @since 2020/9/7
 */
@Repository
public class TransferReceiveDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferReceiveDao.class);

    // 在内存里面，简单的模拟数据库
    private final Map<String, TransferSession> sessionMap = new ConcurrentHashMap<>(16);

    public TransferSession get(String transferNo) {
        return sessionMap.get(transferNo);
    }

    public void insert(TransferSession session) {
        TransferSession old = sessionMap.putIfAbsent(session.getTransferNo(), session);
        if (old != null) {
            LOGGER.warn("交易号：{} 已存在，忽略", session.getTransferNo());
        }
    }

}
