package com.example.ratelimit.exception;

import java.text.MessageFormat;

/**
 * 限流-自定义异常
 *
 * @author 程序员小强
 */
public class RateLimitException extends RuntimeException {
    protected String msg;
    protected Integer code;

    public RateLimitException(String message) {
        super(message);
    }

    protected RateLimitException(Integer code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getCode() {
        return this.code;
    }
}