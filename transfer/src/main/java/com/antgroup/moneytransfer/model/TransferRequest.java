package com.antgroup.moneytransfer.model;

import java.math.BigDecimal;

/**
 * TransferRequest
 * 转账请求
 *
 * @author henng
 * @since 2020/9/6
 */
public class TransferRequest {

    // 一次转账中的唯一标识，交易号、流水号
    private String transferNo;

    // 转账时间
    private long timestamp;

    // 转账金额
    private BigDecimal amount;

    // 转出账号
    private String transferFrom;

    // 转入账号
    private String transferTo;

    // token
    private String token;

    public String getTransferNo() {
        return transferNo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public TransferRequest setTransferNo(String transferNo) {
        this.transferNo = transferNo;
        return this;
    }

    public TransferRequest setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TransferRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransferRequest setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
        return this;
    }

    public TransferRequest setTransferTo(String transferTo) {
        this.transferTo = transferTo;
        return this;
    }

    public String getToken() {
        return token;
    }

    public TransferRequest setToken(String token) {
        this.token = token;
        return this;
    }
}
