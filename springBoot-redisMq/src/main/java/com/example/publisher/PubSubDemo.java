/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.example.publisher;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PubSubDemo {
    public static void main(String[] args) {
        // 连接redis服务端
        JedisPool jedisPool =
                new JedisPool(new JedisPoolConfig(), "101.37.78.57", 6699, 3000, "PbuFNjIt3iOUQdXKsjPcmR2oq0jlC3Lx6S1+Nfm9ZCc=");

        System.out.println(String.format("redis pool is starting, redis ip %s, redis port %d", "127.0.0.1", 6699));

        //订阅者
        SubThread subThread = new SubThread(jedisPool);
        subThread.start();

        //发布者
        Publisher publisher = new Publisher(jedisPool);
        publisher.start();
    }


}