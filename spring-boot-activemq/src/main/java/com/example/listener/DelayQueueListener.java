package com.example.listener;

import cn.hutool.core.date.DateUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Date;

/**
 * 延时队列-消费者监听
 *
 * @author 程序员小强
 * @date 2020-08-30
 */
@Component
public class DelayQueueListener {


    /**
     * 接收queue类型消息
     * destination对应配置类中ActiveMQQueue("springboot.queue")设置的名字
     *
     * @throws Exception
     */
    @JmsListener(destination = "springboot.delay.queue", containerFactory = "jmsDelayQueueListener")
    public void listenQueue(ObjectMessage message, Session session) throws JMSException {
        try {
            System.out.println("********** 延时队列 springboot.delay.queue **********");
            System.out.println("消息ID: " + message.getJMSMessageID() + " 当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
            System.out.println("线程名称: " + Thread.currentThread().getName());
            System.out.println("消息内容: " + message.getObject().toString());
            System.out.println("********** 延时队列 springboot.delay.queue **********");
            System.out.println();
            message.acknowledge();
        } catch (Exception e) {
            System.out.println(message.getJMSMessageID() + e.getMessage());
            //消息重试
            session.recover();
        }
    }
}