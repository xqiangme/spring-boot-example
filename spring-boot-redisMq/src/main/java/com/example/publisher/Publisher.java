/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.example.publisher;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Publisher extends Thread {
    private final JedisPool jedisPool;

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Jedis jedis = jedisPool.getResource();   //连接池中取出一个连接
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
                if (!"quit".equals(line)) {
                    jedis.publish("mychannel", line);   //从 mychannel 的频道上推送消息
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}