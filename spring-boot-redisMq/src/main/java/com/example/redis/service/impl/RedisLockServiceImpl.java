package com.example.redis.service.impl;

import com.example.redis.client.RedisClient;
import com.example.redis.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * Redis 分布式锁-相关操作接口
 *
 * @author mengqiang
 */
@Component
public class RedisLockServiceImpl implements RedisLockService {

    private static final Long ONE = 1L;
    private static final String OK = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisClient redisClient;

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey           锁key
     * @param requestId         (请求标识)加锁者标识
     * @param expireMillisecond 超期时间 毫秒数
     * @return 是否获取成功
     */
    @Override
    public boolean tryGetLock(String lockKey, String requestId, int expireMillisecond) {
        return redisClient.invoke(jedisPool, jedis -> {
                    String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireMillisecond);
                    return OK.equals(result);
                }
        );
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁key
     * @param requestId (请求标识)加锁者标识
     * @return 是否释放成功
     */
    @Override
    public boolean releaseLock(String lockKey, String requestId) {
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return redisClient.invoke(jedisPool, jedis -> {
                    Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
                    return ONE.equals(result);
                }
        );
    }

}