package com.antgroup.moneytransfer.test;

import com.antgroup.moneytransfer.dao.TransferDao;
import com.antgroup.moneytransfer.dao.UserDao;
import com.antgroup.moneytransfer.model.TransferRequest;
import com.antgroup.moneytransfer.model.TransferResponse;
import com.antgroup.moneytransfer.model.User;
import com.antgroup.moneytransfer.service.impl.TransferServiceImpl;
import com.antgroup.moneytransfer.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * TransferTest
 *
 * @author henng
 * @since 2020/9/7
 */
public class TransferTest {

    @Test
    public void testMoneyTransfer() {
        // 初始化两个用户
        User user1 = new User()
            .setId(3)
            .setCardNo("3333")
            .setBalance(new BigDecimal("2000.00"));

        User user2 = new User()
            .setId(4)
            .setCardNo("4444")
            .setBalance(new BigDecimal("1000.00"));


        UserDao userDao = new UserDao();
        userDao.update(user1);
        userDao.update(user2);

        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao);
        TransferDao transferDao = new TransferDao();

        TransferServiceImpl transferService = new TransferServiceImpl();
        transferService.setUserService(userService);
        transferService.setTransferDao(transferDao);

        TransferRequest transferRequest = new TransferRequest()
            .setTransferNo("No.202009070001")
            .setTransferFrom("3333")
            .setTransferTo("4444")
            .setAmount(new BigDecimal("200.00"))
            .setToken("token1")
            .setTimestamp(System.currentTimeMillis());

        TransferResponse transferResponse = transferService.transfer(transferRequest);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(transferResponse.getErrorMsg().getMessage());

        Assert.assertTrue(transferResponse.isSuccess());
        Assert.assertEquals(userDao.get("3333").getBalance(), new BigDecimal("1800.00"));  //2000-200
        Assert.assertEquals(userDao.get("4444").getBalance(), new BigDecimal("1200.00"));  //1000+200

    }
}
