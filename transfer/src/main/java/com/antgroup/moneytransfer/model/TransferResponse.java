package com.antgroup.moneytransfer.model;

import java.io.Serializable;

/**
 * TransferResponse
 * 转账返回结果
 *
 * @author henng
 * @since 2020/9/6
 */
public class TransferResponse implements Serializable {

    private static final long serialVersionUID = 146435932952070513L;

    // 是否成功
    private boolean success;

    // 不成功时，错误信息
    private ErrorMsg errorMsg;

    public TransferResponse(boolean success, ErrorMsg errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
    }
}
