package com.example.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 广播模式-消息监听
 *
 * @author 程序员小强
 * @date 2020-08-30
 */
@Component
public class TopicQueueListener {

    /**
     * 接收topic类型消息
     * destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
     * containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
     *
     * @param msg
     */
    @JmsListener(destination = "springboot.topic", containerFactory = "jmsTopicListener")
    public void listenTopic01(String msg) {
        System.out.println("********** springboot.topic01 **********");
        System.out.println("线程名称: " + Thread.currentThread().getName());
        System.out.println("消息内容: " + msg);
        System.out.println("********** springboot.topic01 **********");
        System.out.println();
    }

    /**
     * 接收topic类型消息
     * destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
     * containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
     *
     * @param msg
     */
    @JmsListener(destination = "springboot.topic", containerFactory = "jmsTopicListener")
    public void listenTopic02(String msg) {
        System.out.println("********** springboot.topic02 **********");
        System.out.println("线程名称: " + Thread.currentThread().getName());
        System.out.println("消息内容: " + msg);
        System.out.println("********** springboot.topic02 **********");
        System.out.println();
    }
}