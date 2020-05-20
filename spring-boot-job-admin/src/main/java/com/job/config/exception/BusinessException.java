package com.job.config.exception;

import com.job.admin.enums.ExceptionEnumInterface;

/**
 * 业务异常
 *
 * @author mengq
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ExceptionEnumInterface enums, Object... args) {
        super(enums.getCode(), enums.getMsg(), args);
    }

}