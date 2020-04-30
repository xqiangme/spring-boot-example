/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.example.publisher;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubThread extends Thread {
    private final JedisPool jedisPool;

    private final Subscriber subscriber = new Subscriber();
    private final String channel = "mychannel";

    public SubThread(JedisPool jedisPool) {
        super("SubThread");
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        Jedis jedis = null;
        try {
            //取出一个连接
            jedis = jedisPool.getResource();
            //通过subscribe 的api去订阅，入参是订阅者和频道名
            jedis.subscribe(subscriber, channel);
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}