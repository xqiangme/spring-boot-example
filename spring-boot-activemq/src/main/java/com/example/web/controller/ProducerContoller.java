package com.example.web.controller;

import cn.hutool.core.date.DateUtil;
import com.example.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@Slf4j
@RestController
public class ProducerContoller {

    @Autowired
    private Producer producer;

    @Autowired
    private Queue queue;

    @Autowired
    private Queue delayQueue;

    @Autowired
    private Topic topic;

    /**
     * 发送queue类型消息
     *
     * @param msg
     */
    @GetMapping("/queue")
    public void sendQueueMsg(String msg) {
        producer.send(queue, msg);
    }

    /**
     * 发送延时类型消息
     *
     * @param msg
     */
    @GetMapping("/delayQueue")
    public void sendDelayQueueMsg(String msg) {
        log.info("[ 延时消息发送 ] 当前时间：{}", DateUtil.now());
        producer.delaySend(delayQueue, msg, 1000L * 5);
    }

    /**
     * 发送topic类型消息
     *
     * @param msg
     */
    @GetMapping("/topic")
    public void sendTopicMsg(String msg) {
        producer.send(topic, msg);
    }

}