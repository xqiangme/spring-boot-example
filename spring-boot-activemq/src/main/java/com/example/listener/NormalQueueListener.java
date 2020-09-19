package com.example.listener;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * 消息监听
 *
 * @author 程序员小强
 * @date 2020-09-19
 */
@Slf4j
@Component
public class NormalQueueListener {

  @JmsListener(destination = "springboot.queue1")
  public void receiveMsg1(String message) {
    Thread thread = Thread.currentThread();
    log.info("[ 普通消息消费 ] >> 线程ID:{},线程名称:{},消息内容:{}", thread.getId(), thread.getName(), message);
  }


  /***
   * 普通消息-多线程消费实例
   *
   * @param message
   */
  @JmsListener(destination = "springboot.queue2", containerFactory = "jmsQueue2Listener")
  public void receiveMsg2(String message) {
    Thread thread = Thread.currentThread();
    log.info("[ 普通消息多线程消费 ] >> 线程ID:{},线程名称:{},消息内容:{}", thread.getId(), thread.getName(), message);
  }

  /**
   * 消息监听
   *
   * @throws Exception
   */
  @JmsListener(destination = "springboot.queue3", containerFactory = "jmsQueue3Listener")
  public void listenQueue(final TextMessage message, Session session) throws JMSException {
    try {
      Thread thread = Thread.currentThread();
      log.info("********** 华丽的分割线 **********");
      log.info("[ 普通消息消费-完善案例 ] >> 当前时间:{}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
      log.info("[ 普通消息消费-完善案例 ] >> 消息ID:{},消息内容:{}", message.getJMSMessageID(), message.getText());
      log.info("[ 普通消息消费-完善案例 ] >> 线程ID:{},线程名称:{}", thread.getId(), thread.getName());
      log.info("********** 华丽的分割线 **********");
      log.info("");

//      //模拟异常
//      if (1 == 1) {
//        throw new RuntimeException("测试异常了");
//      }
      //使用手动签收模式，需要手动的调用，如果不在catch中调用session.recover()消息只会在重启服务后重发
      message.acknowledge();
      log.info("[ 普通消息消费-完善案例 ] >> 消费成功了 , 消息ID:{},消息内容:{}", message.getJMSMessageID(), message.getText());
      log.info("********** 华丽的分割线-消费成功了 **********");
      log.info("");
    } catch (Exception e) {
      log.info("[ 普通消息消费-完善案例 ] >> 消费异常了 , 消息ID:{},消息内容:{}",
        message.getJMSMessageID(), message.getText(), e);
      //消息重试
      session.recover();
    }
  }
}
