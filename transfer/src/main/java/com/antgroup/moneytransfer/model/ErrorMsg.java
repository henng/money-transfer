package com.antgroup.moneytransfer.model;

/**
 * ErrorMsg
 *
 * @author henng
 * @since 2020/9/7
 */
public enum ErrorMsg {

    SUCCESS(0, "成功"),
    ILLEGAL_PARAM(1, "参数错误"), // 可以再细分具体的参数
    ILLEGAL_REQUEST(2, "非法请求"),
    DUPLICATE_TRANSFER(3, "交易已存在"),
    BALANCE_INSUFFICIENT(4, "余额不足"),
    USER_NOT_EXISTS(5, "用户不存在");

    private int code;
    private String message;

    ErrorMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
