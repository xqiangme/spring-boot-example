package com.example.listener;

import redis.clients.jedis.JedisPubSub;

/**
 * @author mengqiang
 */
public class MyRedisMsgListener extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId() + " > " + thread.getName() + "------- channel is " + channel + " message is " + message);
    }
}