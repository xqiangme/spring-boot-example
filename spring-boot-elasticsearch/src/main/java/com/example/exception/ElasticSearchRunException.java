package com.example.exception;


/**
 * es 执行异常
 *
 * @author 程序员小强
 */
public class ElasticSearchRunException extends BaseException {

    public ElasticSearchRunException(String message) {
        super(message);
    }

    public ElasticSearchRunException(String mess, Object... args) {
        super(mess, args);
    }

}
