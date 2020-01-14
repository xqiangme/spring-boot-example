package com.example.exception;


/**
 * es 执行异常
 *
 * @author mengqiang
 */
public class ElasticsearchRunException extends BaseException {

    public ElasticsearchRunException(String message) {
        super(message);
    }

    public ElasticsearchRunException(String mess, Object... args) {
        super(mess, args);
    }

}