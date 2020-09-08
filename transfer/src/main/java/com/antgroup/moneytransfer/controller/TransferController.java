package com.antgroup.moneytransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.antgroup.moneytransfer.model.TransferRequest;
import com.antgroup.moneytransfer.model.TransferResponse;
import com.antgroup.moneytransfer.service.TransferService;

/**
 * TransferController
 *
 * @author henng
 * @since 2020/9/7
 */
@Controller
@RequestMapping(value = "/api")
public class TransferController {
    @Autowired
    TransferService transferService;


    @ResponseBody
    @RequestMapping(value = "/money/transfer", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    public TransferResponse moneyTransfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}
