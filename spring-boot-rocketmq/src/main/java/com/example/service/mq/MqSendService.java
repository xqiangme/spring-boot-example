package com.example.service.mq;

/**
 * 消息队列-发送接口
 *
 * @author 程序员小强
 */
public interface MqSendService {

    /**
     * 发送普通消息
     *
     * @param content 消息内容
     */
    void senNormalMsg(Object content);


}