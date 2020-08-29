package com.example.pubsub.subscribe;

import com.example.pubsub.RedisPubSubClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
@Getter
@Setter
public class UserRegisterMsgSub extends Thread {

    private String channel;
    private RedisPubSubClient redisPubSubClient;

    public UserRegisterMsgSub(String channel, RedisPubSubClient redisPubSubClient) {
        this.channel = channel;
        this.redisPubSubClient = redisPubSubClient;
    }

    @Override
    public void run() {
        UserRegisterMsgSub.SubHandler subHandler = new UserRegisterMsgSub.SubHandler();
        redisPubSubClient.subscribe(channel, subHandler);
        log.info("[ 注册-用户新增消息订阅者 ] channel:{}", channel);
    }

    public static class SubHandler extends JedisPubSub {

        /**
         * 收到消息会调用
         * <p>
         * 注意：消息要做幂等性处理-当多个订阅者时，每个订阅者都会收到
         *
         * @param channel
         * @param message
         */
        @Override
        public void onMessage(String channel, String message) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //订阅消息
            log.info("[ 用户新增消息订阅 ] onMessage threadName:{},channel:{},message:{} ", Thread.currentThread().getName(), channel, message);

            // todo something 这里执行可以执行业务代码
        }

        /**
         * 订阅了频道会调用
         */
        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            log.info("[ 用户新增频道订阅 ] 订阅了频道 threadName:{},channel:{},subscribedChannels:{}", Thread.currentThread().getName(), channel, subscribedChannels);
        }

        /**
         * 取消订阅 会调用
         */
        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
            log.info("[ 用户新增频道解除订阅 ] onUnsubscribe threadName:{},channel:{},subscribedChannels:{}", Thread.currentThread().getName(), channel, subscribedChannels);
        }
    }
}