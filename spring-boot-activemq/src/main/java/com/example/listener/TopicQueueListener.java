package com.example.listener;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 广播模式-消息监听
 *
 * @author 程序员小强
 * @date 2020-09-19
 */
@Slf4j
@Component
public class TopicQueueListener {

  /**
   * 接收topic类型消息
   * destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
   * containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
   *
   * @param message
   */
  @JmsListener(destination = "springboot.topic", containerFactory = "jmsTopicListener")
  public void receiveMsg1(String message) {
    Thread thread = Thread.currentThread();
    log.info("[ Topic消息消费01 ] >> 线程ID:{},线程名称:{},消息内容:{},消费时间:{}", thread.getId(), thread.getName(), message, DateUtil.now());
  }

  @JmsListener(destination = "springboot.topic", containerFactory = "jmsTopicListener")
  public void receiveMsg2(String message) {
    Thread thread = Thread.currentThread();
    log.info("[ Topic消息消费02 ] >> 线程ID:{},线程名称:{},消息内容:{},消费时间:{}", thread.getId(), thread.getName(), message, DateUtil.now());
  }

}
