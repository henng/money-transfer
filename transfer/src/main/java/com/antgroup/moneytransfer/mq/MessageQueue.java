package com.antgroup.moneytransfer.mq;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.antgroup.moneytransfer.model.TransferSession;

/**
 * MessageQueue
 * 模拟 MQ
 *
 * @author henng
 * @since 2020/9/7
 */
public class MessageQueue {
    ConcurrentLinkedQueue<TransferSession> queue = new ConcurrentLinkedQueue<>();

    /**
     * 真实 MQ 一般会有异常抛出，可能需要处理
     * @param message 消息
     * @throws Exception 发送失败异常
     */
    public void send(TransferSession message) throws Exception {
        queue.offer(message);
    }

    public TransferSession pull() {
        return queue.peek();
    }

    public void ack(TransferSession message) {
        queue.remove(message);
    }
}
