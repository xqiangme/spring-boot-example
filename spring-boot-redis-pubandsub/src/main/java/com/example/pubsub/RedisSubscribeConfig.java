package com.example.pubsub;

import com.example.pubsub.enums.MessesChannel;
import com.example.pubsub.subscribe.KeyExpiredSub;
import com.example.pubsub.subscribe.OrderAddMsgSub;
import com.example.pubsub.subscribe.UserRegisterMsgSub;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Redis - 消息订阅组件
 *
 * @author 程序员小强
 */
@Component
public class RedisSubscribeConfig {

    /**
     * 用户注册消息-订阅者
     *
     * @param redisPubSubClient
     */
    @Bean
    public UserRegisterMsgSub userRegisterMsgConsumer(@Qualifier("redisPubSubClient") RedisPubSubClient redisPubSubClient) {
        UserRegisterMsgSub messesConsumer = new UserRegisterMsgSub(MessesChannel.USER_REGISTER.name(), redisPubSubClient);
        messesConsumer.start();
        return messesConsumer;
    }

    /**
     * 订单创建消息-订阅者
     *
     * @param redisPubSubClient
     */
    @Bean
    public OrderAddMsgSub orderAddMsgConsumer(@Qualifier("redisPubSubClient") RedisPubSubClient redisPubSubClient) {
        OrderAddMsgSub messesConsumer = new OrderAddMsgSub(MessesChannel.ORDER_ADD.name(), redisPubSubClient);
        messesConsumer.start();
        return messesConsumer;
    }

    /**
     * key过期失效-订阅者
     *
     * @param redisPubSubClient
     */
    @Bean
    public KeyExpiredSub keyExpiredSub(@Qualifier("redisPubSubClient") RedisPubSubClient redisPubSubClient) {
        //__key*__:*
        // __keyevent@0__:expired 艾特后面的0表示第几个是数据库
        KeyExpiredSub messesConsumer = new KeyExpiredSub(" __keyevent@0__:expired", redisPubSubClient);
        messesConsumer.start();
        return messesConsumer;
    }
}