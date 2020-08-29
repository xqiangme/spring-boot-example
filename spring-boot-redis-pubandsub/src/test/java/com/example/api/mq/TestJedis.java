/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.api.mq;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author mengq
 * @date 2020-07-31 15:11
 * @desc
 */
public class TestJedis {

    public static void main(String[] args) {
        // 连接redis服务端
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 3000, "123456");
        Jedis jedis = pool.getResource();
        jedis.set("notify", "umq");
        jedis.expire("notify", 10);
    }

}