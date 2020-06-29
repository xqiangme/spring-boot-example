package com.example.exception;

import java.text.MessageFormat;

/**
 * 自定义异常
 *
 * @author 程序员小强
 */
public class RedisBloomFilterException extends RuntimeException {
    protected String msg;
    protected Integer code;

    public RedisBloomFilterException(String message) {
        super(message);
    }

    protected RedisBloomFilterException(Integer code, String msgFormat, Object... args) {
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