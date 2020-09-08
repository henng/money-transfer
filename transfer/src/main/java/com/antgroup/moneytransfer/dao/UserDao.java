package com.antgroup.moneytransfer.dao;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.antgroup.moneytransfer.model.User;

/**
 * UserDao
 *
 * @author henng
 * @since 2020/9/7
 */
@Repository
public class UserDao {
    private final Map<String, User> userMap = new ConcurrentHashMap<>(16);

    public UserDao() {
        init();
    }

    public void init() {
        // 默认初始化两个用户
        User user1 = new User()
            .setId(1)
            .setCardNo("1111")
            .setBalance(new BigDecimal("1000.00"));

        User user2 = new User()
            .setId(2)
            .setCardNo("2222")
            .setBalance(new BigDecimal("800.00"));

        update(user1);
        update(user2);
    }

    public User get(String cardNo) {
        return userMap.get(cardNo);
    }

    public void update(User user) {
        userMap.put(user.getCardNo(), user);
    }
}
