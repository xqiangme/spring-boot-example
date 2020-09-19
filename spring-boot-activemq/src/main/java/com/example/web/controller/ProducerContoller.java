package com.example.web.controller;

import cn.hutool.core.date.DateUtil;
import com.example.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * 消息队列-消息生成接口
 */
@Slf4j
@RestController
public class ProducerContoller {

  @Autowired
  private Producer producer;
  @Autowired
  private Queue normalQueue1;
  @Autowired
  private Queue normalQueue2;
  @Autowired
  private Queue normalQueue3;
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
    log.info("[ 普通消息发送 ] >> msg:{}, 发送时间：{}", msg, DateUtil.now());
    producer.send(normalQueue1, msg);
  }

  /**
   * 发送queue类型-多线程消费实例
   *
   * @param msg
   */
  @GetMapping("/queue2")
  public void sendQueueMsg2(String msg) {
    log.info("[ 普通消息发送 ] >> msg:{}, 发送时间：{}", msg, DateUtil.now());
    producer.send(normalQueue2, msg);
  }

  /**
   * 发送queue类型消息
   *
   * @param msg
   */
  @GetMapping("/queue3")
  public void sendQueueMsg3(String msg) {
    log.info("[ 普通消息发送 ] >> msg:{}, 发送时间：{}", msg, DateUtil.now());
    producer.send(normalQueue3, msg);
  }

  /**
   * 发送延时类型消息
   *
   * @param msg
   */
  @GetMapping("/delayQueue")
  public void sendDelayQueueMsg(String msg) {
    log.info("[ 延时消息发送 ] >> msg:{}, 发送时间：{}", msg, DateUtil.now());
    producer.delaySend(delayQueue, msg, 1000L * 10);
  }

  /**
   * 发送topic类型消息
   *
   * @param msg
   */
  @GetMapping("/topic")
  public void sendTopicMsg(String msg) {
    log.info("[ Topic消息发送 ] >> msg:{}, 发送时间：{}", msg, DateUtil.now());
    producer.send(topic, msg);
  }

}
