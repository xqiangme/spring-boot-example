package com.example.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息监听-辅助工具类
 *
 * @author 程序员小强
 */
public class MqListenerHelperUtil {

    /**
     * 默认重试次数 3
     */
    private static final int DEFAULT_RETRY_TIMES = 3;

    /**
     * 获取字符串类型消息体
     *
     * @param message 消息信息
     */
    public static String parseStringMsg(Message message) {
        if (null == message.getBody()) {
            return "";
        }
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }

    /**
     * 格式化消息为对象
     *
     * @param message 消息
     */
    public static <T> T parseObjectMsg(Message message, Class<T> clazz) {
        if (null == message.getBody() || message.getBody().length == 0) {
            return JSON.parseObject("{}", clazz);
        }
        return JSON.parseObject(message.getBody(), clazz);
    }

    /**
     * 格式化消息为列表
     *
     * @param message 消息
     * @param clazz
     */
    public static <T> List<T> parseArrayMsg(Message message, Class<T> clazz) {
        if (null == message.getBody() || message.getBody().length == 0) {
            return JSON.parseArray("[]", clazz);
        }
        return JSON.parseArray(parseStringMsg(message), clazz);
    }

    /**
     * 是否可以重试 , 默认3次 ;
     *
     * @param runTime 当前执行次数
     */
    public static Boolean canRetryTimes(int runTime) {
        return canRetryTimes(runTime, DEFAULT_RETRY_TIMES);
    }

    /**
     * 是否可以重试;
     *
     * @param runTime    当前执行次数
     * @param retryTimes 重试次数
     */
    public static Boolean canRetryTimes(int runTime, int retryTimes) {
        return (runTime < retryTimes);
    }

}