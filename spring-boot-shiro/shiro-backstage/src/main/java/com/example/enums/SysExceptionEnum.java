package com.example.enums;

public enum SysExceptionEnum implements ExceptionEnumInterface {

    /**
     * 异常枚举
     */
    OK("2000", "操作成功"),
    INVALID_PARAM("3000", "参数校验错误"),

    USER_NOT_EXIST("4001", "用户不存在"),
    USER_PASSWORD_ERROR("4002", "用户名或密码错误"),
    USER_ACCESS_DENIED("4003", "访问权限不足"),
    USER_LOGIN_ERROR("4004", "登录其它异常"),
    SYSTEM_NOT_LOGIN_ERROR("4005", "未登录 ，请先登录"),

    SYSTEM_ERROR("6000", "网络繁忙，请稍后再试"),

    ;

    SysExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}