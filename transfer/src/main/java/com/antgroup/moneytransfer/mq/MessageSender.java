package com.antgroup.moneytransfer.mq;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.antgroup.moneytransfer.dao.TransferDao;
import com.antgroup.moneytransfer.model.TransferSession;

/**
 * MessageSender
 *
 * @author henng
 * @since 2020/9/7
 */
public class MessageSender {
    @Autowired
    private TransferDao transferDao;

    private final MessageQueue messageQueue;

    private static final ScheduledExecutorService sender = Executors.newScheduledThreadPool(1);

    public MessageSender(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void setTransferDao(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    public void start() {
        MessageSentChecker messageSentChecker = new MessageSentChecker();

        sender.scheduleWithFixedDelay(messageSentChecker, 1, 1, TimeUnit.SECONDS);
    }

    private class MessageSentChecker implements Runnable {
        @Override
        public void run() {
            Collection<TransferSession> unsentMessages = transferDao.getUnsentMessages();
            if (unsentMessages.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }

            // 实际情况可能有消息发送失败的，下次可以重试
            for (TransferSession m : unsentMessages) {
                try {
                    messageQueue.send(m);
                } catch (Exception e) {
                    continue;
                }

                m = m.setHasSentMq(true)
                    .setTransferFinishTime(System.currentTimeMillis());
                transferDao.update(m);
            }
        }
    }
}
