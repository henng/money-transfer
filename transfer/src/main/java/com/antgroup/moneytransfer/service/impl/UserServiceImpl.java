package com.antgroup.moneytransfer.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antgroup.moneytransfer.dao.UserDao;
import com.antgroup.moneytransfer.model.ErrorMsg;
import com.antgroup.moneytransfer.model.User;
import com.antgroup.moneytransfer.service.UserService;

/**
 * UserServiceImpl
 *
 * @author henng
 * @since 2020/9/7
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void transferIn(String cardNo, BigDecimal amount) {
        User user = userDao.get(cardNo);
        if (user == null) {
            LOGGER.error("用户：{} 账号不存在", cardNo);
            throw new RuntimeException(ErrorMsg.USER_NOT_EXISTS.getMessage());
        }

        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);
        userDao.update(user);
    }

    @Override
    public void transferOut(String cardNo, BigDecimal amount) {
        User user = userDao.get(cardNo);
        if (user == null) {
            LOGGER.error("用户：{} 账号不存在", cardNo);
            throw new RuntimeException(ErrorMsg.USER_NOT_EXISTS.getMessage());
        }

        if (user.getBalance().compareTo(amount) < 0) {
            LOGGER.error("用户：{} 余额不足", cardNo);
            throw new RuntimeException(ErrorMsg.BALANCE_INSUFFICIENT.getMessage());
        }

        BigDecimal newBalance = user.getBalance().subtract(amount);
        user.setBalance(newBalance);
        userDao.update(user);
    }
}
