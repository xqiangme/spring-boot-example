/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.example.config;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * @author mengqiang
 * @version .java, v 0.1   -   -
 */
public class RedisMQClient {
    private static final String CHANNEL = "default.channel";

    private final JedisPool jedisPool;

    public RedisMQClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 发布消息
     *
     * @param channel
     * @param messages
     */
    public void publish(String channel, String messages) {
        Jedis jedis = null;
        if (StringUtils.isBlank(channel)) {
            channel = CHANNEL;
        }
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, messages);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     * 订阅消息
     *
     * @param channel
     * @param jedisPubSub
     */
    public void subscribe(String channel, JedisPubSub jedisPubSub) {

        Jedis jedis = null;
        if (StringUtils.isBlank(channel)) {
            channel = CHANNEL;
        }

        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //默认连接本地redis,
        // 连接redis服务端
        JedisPool jedisPool =
                new JedisPool(new JedisPoolConfig(), "101.37.78.57", 6699, 3000, "PbuFNjIt3iOUQdXKsjPcmR2oq0jlC3Lx6S1+Nfm9ZCc=", 2);

        RedisMQClient publish = new RedisMQClient(jedisPool);

        new Thread(new Runnable() {
            @Override
            public void run() {
                publish.subscribe("PID", new MyjedisPubSub());
            }
        }).start();

        for (int i = 0; i < 100; i++) {
            publish.publish("PID", "1_messge" + i);
        }

        Thread.sleep(1000);
        for (int i = 0; i < 30; i++) {
            publish.publish("PID", "2_messge" + i);
        }

    }

    public static class MyjedisPubSub extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            System.out.println("-------channel is " + channel + " message is " + message);
        }
    }
}