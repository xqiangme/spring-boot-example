package com.example.provide.exception;

/**
 * 异常枚举
 */
public enum SysExceptionEnum {


    /**
     * 异常枚举
     */
    OK("2000", "操作成功"),
    SYSTEM_ERROR("4000", "网络繁忙，请稍后再试"),
    PARAM_SIGN_ERROR("4001", "参数签名错误"),
    SYSTEM_BUSY("4005", "系统繁忙"),
    INVALID_PARAM("4002", "参数校验错误"),
    ;

    SysExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}