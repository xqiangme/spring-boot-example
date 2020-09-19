package com.example.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;
import javax.jms.Topic;


/**
 * 消息队列配置
 *
 * @author 程序员小强
 * @date 2020-09-19
 */
@Configuration
public class ActiveMQConfig {

  /**
   * 声明普通队列1
   */
  @Bean
  public Queue normalQueue1() {
    return new ActiveMQQueue("springboot.queue1");
  }

  /**
   * 声明普通队列2
   */
  @Bean
  public Queue normalQueue2() {
    return new ActiveMQQueue("springboot.queue2");
  }

  /**
   * 声明普通队列3
   */
  @Bean
  public Queue normalQueue3() {
    return new ActiveMQQueue("springboot.queue3");
  }

  /**
   * 声明延时队列
   */
  @Bean
  public Queue delayQueue() {
    return new ActiveMQQueue("springboot.delay.queue");
  }


  /**
   * 声明广播队列
   */
  @Bean
  public Topic topic() {
    return new ActiveMQTopic("springboot.topic");
  }
}
