package com.example.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送-辅助工具
 *
 * @author 程序员小强
 */
@Slf4j
public class MqSendHelperUtil {

    /**
     * [同步]-发送普通消息
     *
     * @param producer 生产者实例
     * @param topic    主题
     * @param tags     标签
     * @param content  消息内容
     */
    public static SendResult send(Producer producer, String topic, String tags, Object content) {
        return send(producer, topic, tags, null, content, null, null);
    }

    /**
     * [同步]-发送普通消息
     *
     * @param producer 生产者实例
     * @param topic    主题
     * @param tags     标签
     * @param content  消息内容
     */
    public static SendResult send(Producer producer, String topic, String tags, Object key, Object content) {
        return send(producer, topic, tags, key, content, null, null);
    }

    /**
     * [同步]-发送延时消息-（单位默认分）
     *
     * @param producer    生产者实例
     * @param topic       主题
     * @param tags        标签
     * @param content     消息内容
     * @param deliverTime 延时时间-配合单位TimeUnit使用
     */
    public static SendResult sendDelayMinutes(Producer producer, String topic, String tags, Object content,
                                              Integer deliverTime) {
        return send(producer, topic, tags, null, content, deliverTime, TimeUnit.MINUTES);
    }

    /**
     * [同步]-发送延时消息-（单位默认分）
     *
     * @param producer 生产者实例
     * @param topic    主题
     * @param tags     标签
     * @param content  消息内容
     * @param minutes  延时分钟数
     */
    public static SendResult sendDelayMinutes(Producer producer, String topic, String tags, Object key, Object content,
                                              Integer minutes) {
        return send(producer, topic, tags, key, content, minutes, TimeUnit.MINUTES);
    }

    /**
     * [同步]-发送延时消息-自定义时间单位
     *
     * @param producer    生产者实例
     * @param topic       主题
     * @param tags        标签
     * @param content     消息内容
     * @param deliverTime 延时时间-配合单位TimeUnit使用
     * @param timeUnit    延时时间单位
     */
    public static SendResult sendDelay(Producer producer, String topic, String tags, Object content,
                                       Integer deliverTime, TimeUnit timeUnit) {
        return send(producer, topic, tags, null, content, deliverTime, timeUnit);
    }

    /**
     * [同步]-发送延时消息-自定义时间单位
     *
     * @param producer    生产者实例
     * @param topic       主题
     * @param tags        标签
     * @param content     消息内容
     * @param deliverTime 延时时间-配合单位TimeUnit使用
     * @param timeUnit    延时时间单位
     */
    public static SendResult sendDelay(Producer producer, String topic, String tags, Object key, Object content,
                                       Integer deliverTime, TimeUnit timeUnit) {
        return send(producer, topic, tags, key, content, deliverTime, timeUnit);
    }

    /**
     * [同步]-发送消息
     *
     * @param producer    生产者实例
     * @param topic       主题
     * @param tags        标签
     * @param content     消息内容
     * @param deliverTime 延时时间-配合单位TimeUnit使用
     * @param timeUnit    延时时间单位
     */
    public static SendResult send(Producer producer, String topic, String tags, Object key, Object content,
                                  Integer deliverTime, TimeUnit timeUnit) {
        try {
            //序列化消息内容
            byte[] body = JSON.toJSONString(content).getBytes(StandardCharsets.UTF_8);
            //构建消息体
            Message message = new Message(topic, tags, body);
            //消息指定业务Key
            if (null != key && !"".equals(key)) {
                message.setKey(String.valueOf(key));
            }
            //延时队列-延时到
            if (null != deliverTime && null != timeUnit) {
                message.setStartDeliverTime(new Date(timeUnit.toMillis(deliverTime)).getTime());
            }
            //执行发送
            SendResult result = producer.send(message);
            log.info("sync send success  topic={} messageId={}", result.getTopic(), result.getMessageId());
            return result;
        } catch (ONSClientException e) {
            log.info("[ MqSend ] >>  sync send exception ", e);
            //出现异常意味着发送失败，同步发送 页面会报错
            return null;
        }
    }

    /**
     * [异步]-发送消息 自定义Callback
     *
     * @param producer     生产者实例
     * @param msg          消息
     * @param sendCallback 回调
     */
    public static void sendAsync(Producer producer, Message msg, SendCallback sendCallback) {
        // 异步发送消息, 发送结果通过 callback 返回给客户端。
        producer.sendAsync(msg, sendCallback);
        // 在 callback 返回之前即可取得 msgId。
        log.info("[ MqSend ] >> send message async. topic={} messageId={}", msg.getTopic(), msg.getMsgID());
    }
}