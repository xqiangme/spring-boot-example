package com.example.exception;

import com.example.enums.ExceptionEnumInterface;

/**
 * 业务异常
 *
 * @author 码农猿
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(ExceptionEnumInterface enums, Object... args) {
        super(enums.getCode(), enums.getMsg(), args);
    }

}