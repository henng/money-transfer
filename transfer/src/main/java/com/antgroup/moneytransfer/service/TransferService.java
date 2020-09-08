package com.antgroup.moneytransfer.service;

import com.antgroup.moneytransfer.model.TransferRequest;
import com.antgroup.moneytransfer.model.TransferResponse;

/**
 * TransferService
 *
 * @author henng
 * @since 2020/9/7
 */
public interface TransferService {

    /**
     * 转账接口
     *
     * @param request 转账请求
     *
     * @return 转账结果
     */
    TransferResponse transfer(TransferRequest request);
}
