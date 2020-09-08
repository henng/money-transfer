package com.antgroup.moneytransfer.service;

import java.math.BigDecimal;

/**
 * UserService
 *
 * @author henng
 * @since 2020/9/7
 */
public interface UserService {

    /**
     * 转账转入
     *
     * @param cardNo 用户账号
     * @param amount 转账金额
     */
    void transferIn(final String cardNo, BigDecimal amount);


    /**
     * 转账转出
     *
     * @param cardNo 用户账号
     * @param amount 转账金额
     */
    void transferOut(final String cardNo, BigDecimal amount);
}
