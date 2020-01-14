package com.example.exception;


/**
 * es 执行异常
 *
 * @author mengqiang
 */
public class ElasticSearchRunException extends BaseException {

    public ElasticSearchRunException(String message) {
        super(message);
    }

    public ElasticSearchRunException(String mess, Object... args) {
        super(mess, args);
    }

}