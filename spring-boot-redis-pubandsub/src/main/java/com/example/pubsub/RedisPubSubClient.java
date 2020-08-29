package com.example.pubsub;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

/**
 * redis 发布订阅client
 *
 * @author 程序员小强
 */
@Component
public class RedisPubSubClient {
    private static final Logger log = LoggerFactory.getLogger(RedisPubSubClient.class);

    /**
     * 默认渠道名
     */
    private static final String CHANNEL = "default.channel";

    /**
     * 连接池
     */
    @Autowired
    public JedisPool jedisPool;

    /**
     * 发布消息
     *
     * @param channel  渠道
     * @param messages 消息内容
     */
    public void publish(String channel, String messages) {
        if (StringUtils.isBlank(channel)) {
            channel = CHANNEL;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, messages);
        } catch (JedisException ex) {
            log.error("[ Redis-publish ] Exception {} - {}", ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }

    /**
     * 订阅消息
     *
     * @param channel     渠道
     * @param jedisPubSub 订阅者
     */
    public void subscribe(String channel, JedisPubSub jedisPubSub) {
        if (StringUtils.isBlank(channel)) {
            channel = CHANNEL;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } catch (JedisException ex) {
            log.error("[ Redis-subscribe ] Exception {} - {}", ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }

    /**
     * 命令订阅一个或多个符合给定模式的频道。
     *
     * @param patterns    频道规则
     * @param jedisPubSub 订阅者
     */
    public void psubscribe(String patterns, JedisPubSub jedisPubSub) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.psubscribe(jedisPubSub, patterns);
        } catch (JedisException ex) {
            log.error("[ Redis-psubscribe ] Exception {} - {}", ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }
}