package com.example.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

/**
 * 消息生产者
 *
 * @author 程序员小强
 * @date 2020-09-19
 */
@Slf4j
@Component
public class Producer {

  @Autowired
  private JmsTemplate jmsTemplate;

  /**
   * 发送消息
   *
   * @param destination 发送到的队列
   * @param message     待发送的消息
   */
  public <T extends Serializable> void send(Destination destination, T message) {
    jmsTemplate.convertAndSend(destination, message);
  }

  /**
   * 延时发送
   *
   * @param destination 发送的队列
   * @param data        发送的消息
   * @param time        延迟时间单号毫秒
   */
  public <T extends Serializable> void delaySend(Destination destination, T data, Long time) {
    Session session = null;
    Connection connection = null;
    MessageProducer producer = null;
    // 获取连接工厂
    ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
    try {
      // 获取连接
      connection = connectionFactory.createConnection();
      connection.start();
      // 获取session，true开启事务，false关闭事务
      session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
      // 创建一个消息队列
      producer = session.createProducer(destination);
      producer.setDeliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
      ObjectMessage message = session.createObjectMessage(data);
      //设置延迟时间
      message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
      // 发送消息
      producer.send(message);
      session.commit();
      log.info("[ 延时消息 ] >> 发送完毕 data:{}", data);
    } catch (Exception e) {
      log.error("[ 发送延时消息 ] 异常 >> data:{}", data, e);
    } finally {
      try {
        if (producer != null) {
          producer.close();
        }
        if (session != null) {
          session.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (Exception e) {
        log.error("[ 发送延时消息 ] 关闭资源异常 >> data{}", data, e);
      }
    }
  }
}
