package com.example.redis.client;

import redis.clients.jedis.Jedis;

import java.io.IOException;

public interface RedisClientInvoker<T> {

    /**
     * invoke
     * @param jedis
     * @return
     * @throws IOException
     */
    T invoke(Jedis jedis) throws IOException;
}
