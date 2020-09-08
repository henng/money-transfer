package com.antgroup.moneytransfer.model;

import java.math.BigDecimal;

/**
 * User
 * 用户
 *
 * @author henng
 * @since 2020/9/6
 */
public class User {

    // id
    private long id;

    // 账号
    private String cardNo;

    // 余额
    private BigDecimal balance;

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getCardNo() {
        return cardNo;
    }

    public User setCardNo(String cardNo) {
        this.cardNo = cardNo;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}
