package com.example.listener;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息监听
 *
 * @author 程序员小强
 * @date 2020-08-30
 */
@Component
public class NormalQueueListener {

    private Map<String, AtomicInteger> count = new HashMap<>();

    /**
     * 接收queue类型消息
     * destination对应配置类中ActiveMQQueue("springboot.queue")设置的名字
     *
     * @throws Exception
     */
//    @JmsListener(destination = "springboot.queue", containerFactory = "jmsQueueListener")
    public void listenQueue(final ObjectMessage message, Session session) throws JMSException {
        try {
            if (null != count.get(message.getJMSMessageID())) {
                count.get(message.getJMSMessageID()).getAndIncrement();
            } else {
                count.put(message.getJMSMessageID(), new AtomicInteger(1));
            }
            System.out.println("********** springboot.queue **********");
            System.out.println("消息ID: " + message.getJMSMessageID() + " 当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
            System.out.println("线程名称: " + Thread.currentThread().getName());
            System.out.println("消息内容: " + message.getObject().toString());
            System.out.println("********** springboot.queue **********");
            System.out.println();
            if (count.get(message.getJMSMessageID()).get() < 5) {
                throw new RuntimeException("测试异常了");
            }
            //使用手动签收模式，需要手动的调用，如果不在catch中调用session.recover()消息只会在重启服务后重发
            message.acknowledge();
            System.out.println("消费成功了: " + message.getObject().toString());
        } catch (Exception e) {
            System.out.println(message.getJMSMessageID() + e.getMessage());
            //消息重试
            session.recover();
        }
    }
}