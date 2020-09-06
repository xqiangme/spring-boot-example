package com.example.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 消息队列核心配置
 *
 * @author 程序员小强
 * @date 2020-08-30
 */
@EnableJms
@Configuration
public class ActiveMQCoreConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    /**
     * 应答模式：单条消息确认模式 activemq 独有
     */
    private static Integer ACKNOWLEDGE_MODE = 4;

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次-设置为5次
        redeliveryPolicy.setMaximumRedeliveries(5);
        //重发时间间隔单位毫秒,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1000L);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory activeMqConnectionFactory(@Qualifier("redeliveryPolicy") RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory activeMqConnectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        activeMqConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMqConnectionFactory;
    }

    @Bean(name = "jmsTemplate")
    public JmsTemplate jmsTemplate(@Qualifier("activeMqConnectionFactory") ActiveMQConnectionFactory activeMqConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        //进行持久化配置 1表示非持久化，2表示持久化
        jmsTemplate.setDeliveryMode(2);
        //客户端签收模式
        jmsTemplate.setSessionAcknowledgeMode(ACKNOWLEDGE_MODE);
        return jmsTemplate;
    }

    @Bean(name = "jmsQueueListener")
    public JmsListenerContainerFactory<?> jmsQueueListener(@Qualifier("activeMqConnectionFactory") ActiveMQConnectionFactory activeMqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(activeMqConnectionFactory);
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(4);
        //连接数
        factory.setConcurrency("5-10");
        //指定任务线程池
        factory.setTaskExecutor(new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy()));
        return factory;
    }

    @Bean(name = "jmsDelayQueueListener")
    public JmsListenerContainerFactory<?> jmsDelayQueueListener(@Qualifier("activeMqConnectionFactory") ActiveMQConnectionFactory activeMqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(activeMqConnectionFactory);
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(ACKNOWLEDGE_MODE);
        //连接数
        factory.setConcurrency("6-10");
        //指定任务线程池
        factory.setTaskExecutor(new ThreadPoolExecutor(6, 10, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy()));
        return factory;
    }

    /**
     * springboot默认只配置queue类型消息，
     * 如果要使用topic类型的消息，则需要配置该bean
     *
     * @return
     */
    @Bean(name = "jmsTopicListener")
    public JmsListenerContainerFactory<?> jmsTopicListener(@Qualifier("activeMqConnectionFactory") ActiveMQConnectionFactory activeMqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMqConnectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        return factory;
    }
}