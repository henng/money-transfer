package com.antgroup.moneytransfer.model;

import java.math.BigDecimal;

/**
 * TransferSession
 * 转账对应的数据库记录
 *
 * @author henng
 * @since 2020/9/7
 */
public class TransferSession {
    // 转账开始时间
    private long transferStartTime;

    // 转账结束时间
    private long transferFinishTime;

    // 流水号
    private String transferNo;

    // 转账金额
    private BigDecimal amount;

    // 转出账号
    private String transferFrom;

    // 转入账号
    private String transferTo;

    // 附加信息
    private String extraMsg;

    // 是否已经发送 mq
    private boolean hasSentMq;

    public long getTransferStartTime() {
        return transferStartTime;
    }

    public TransferSession setTransferStartTime(long transferStartTime) {
        this.transferStartTime = transferStartTime;
        return this;
    }

    public long getTransferFinishTime() {
        return transferFinishTime;
    }

    public TransferSession setTransferFinishTime(long transferFinishTime) {
        this.transferFinishTime = transferFinishTime;
        return this;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public TransferSession setTransferNo(String transferNo) {
        this.transferNo = transferNo;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransferSession setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public TransferSession setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
        return this;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public TransferSession setTransferTo(String transferTo) {
        this.transferTo = transferTo;
        return this;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public TransferSession setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
        return this;
    }

    public boolean isHasSentMq() {
        return hasSentMq;
    }

    public TransferSession setHasSentMq(boolean hasSentMq) {
        this.hasSentMq = hasSentMq;
        return this;
    }
}
