package com.example.pubsub.subscribe;

import com.example.pubsub.RedisPubSubClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
@Getter
@Setter
public class OrderAddMsgSub extends Thread {

    private String channel;
    private RedisPubSubClient redisPubSubClient;

    public OrderAddMsgSub(String channel, RedisPubSubClient redisPubSubClient) {
        this.channel = channel;
        this.redisPubSubClient = redisPubSubClient;
    }

    @Override
    public void run() {
        OrderAddMsgSub.SubHandler subHandler = new OrderAddMsgSub.SubHandler();
        redisPubSubClient.subscribe(channel, subHandler);
        log.info("[ 注册订单新增消息订阅者 ] channel:{}", channel);
    }

    public static class SubHandler extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //订阅消息
            log.info("[ 订单新增消息订阅 ] onMessage channel:{},message:{} ", channel, message);
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            // 订阅了频道 channel
            log.info("[ 订单新增频道订阅 ] 订阅了频道 channel:{},subscribedChannels:{}", channel, subscribedChannels);
        }

        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
            log.info("[ 订单新增频道解除订阅 ] channel:{},subscribedChannels:{}", channel, subscribedChannels);
        }
    }
}