package com.example.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author 程序员小强
 */
public class BaseException extends RuntimeException {

    public String msg;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
        return this.msg;
    }

}
