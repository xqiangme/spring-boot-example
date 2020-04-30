/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.example.config;

import com.example.listener.MyRedisMsgListener;
import com.example.thread.WorkThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * Redis - 消息
 *
 * @author mengqiang
 */
@Component
public class RedisMqConfig {

    @Autowired
    protected JedisPool jedisPool;

    /**
     * 消息-发布者
     */
    @Bean
    public RedisMQClient redisMQClient() {
        System.out.println(null == jedisPool);
        RedisMQClient redisMQClient = new RedisMQClient(jedisPool);
        WorkThreadUtil.getInstance().execute(() -> redisMQClient.subscribe("PID", new MyRedisMsgListener()));
        return redisMQClient;
    }

}