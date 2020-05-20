package com.job.config.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author mengq
 */
public class BaseException extends RuntimeException {
    protected String msg;
    protected Integer code;

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(Integer code, String msgFormat, Object... args) {
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