package com.example.config.mq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.example.config.property.MqProperty;
import com.example.listener.NormalMsgListener;
import com.example.listener.OrderNormalMsgListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * RocketMQ 消息队列-配置
 *
 * @author 程序员小强
 */
@Slf4j
@Configuration
public class MqConfigClient extends MqBaseClient {

    @Autowired
    private MqProperty mqConfig;

    /**
     * 创建-普通消息队列-生产者
     */
    @Bean(name = "msgProducer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean createMsgProducer() {
        return this.createProducer();
    }

    /**
     * 普通消息队列-消费者1
     */
    @Bean(name = "normalMsgConsumer", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean normalMsgConsumer(@Autowired NormalMsgListener msgListener) {
        //订阅配置
        Map<Subscription, MessageListener> subscriptionTable =
                this.createSubscriptionTable(mqConfig.getTopic(), mqConfig.getTag(), msgListener);
        return this.createConsumer(mqConfig.getGroupId(), mqConfig.getConsumeThreadNum(), subscriptionTable);
    }

    /**
     * 订单消息队列-消费者
     */
    @Bean(name = "orderNormalMsgConsumer", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean orderNormalMsgConsumer(@Autowired OrderNormalMsgListener msgListener) {
        //订阅配置
        Map<Subscription, MessageListener> subscriptionTable =
                this.createSubscriptionTable(mqConfig.getOrderTopic(), mqConfig.getOrderTag(), msgListener);
        return this.createConsumer(mqConfig.getOrderGroupId(), mqConfig.getOrderConsumeThreadNum(), subscriptionTable);
    }

}