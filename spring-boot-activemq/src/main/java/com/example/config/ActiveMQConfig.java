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
 * @date 2020-08-30
 */
@Configuration
public class ActiveMQConfig {

    /**
     * 声明普通队列
     */
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("springboot.queue");
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