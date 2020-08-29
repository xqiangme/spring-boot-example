package com.example.pubsub.subscribe;

import com.example.pubsub.RedisPubSubClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

/**
 * redis key 过期监听
 *
 * @author mengq
 * @date 2020-07-31 15:10
 */
@Slf4j
@Getter
@Setter
public class KeyExpiredSub extends Thread {

    private String pattern;
    private RedisPubSubClient redisPubSubClient;

    public KeyExpiredSub(String pattern, RedisPubSubClient redisPubSubClient) {
        this.pattern = pattern;
        this.redisPubSubClient = redisPubSubClient;
    }

    @Override
    public void run() {
        KeyExpiredSub.SubHandler subHandler = new KeyExpiredSub.SubHandler();
        redisPubSubClient.psubscribe(pattern, subHandler);
        log.info("[ key过期监听 ] pattern:{}", pattern);
    }

    public static class SubHandler extends JedisPubSub {
        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
            log.info("[ key过期监听 ] onPSubscribe pattern:{},subscribedChannels:{} ", pattern, subscribedChannels);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            log.info("[ key过期监听 ] onPMessage pattern:{},channel:{},message:{} ", pattern, channel, message);
        }
    }
}