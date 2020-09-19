package com.example.listener;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 延时队列-消费者监听
 *
 * @author 程序员小强
 * @date 2020-09-19
 */
@Slf4j
@Component
public class DelayQueueListener {


  @JmsListener(destination = "springboot.delay.queue")
  public void receiveMsg(String message) {
    Thread thread = Thread.currentThread();
    log.info("[ 延时消息消费 ] >> 线程ID:{},线程名称:{},消息内容:{},消费时间:{}", thread.getId(), thread.getName(), message, DateUtil.now());
  }
}
