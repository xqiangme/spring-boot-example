package com.example.redis.service;

/**
 * Redis 分布式锁-相关操作接口
 *
 * @author mengqiang
 */
public interface RedisLockService {

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey           锁key
     * @param requestId         (请求标识)加锁者标识,示例：随机的UUID
     * @param expireMillisecond 超期时间 毫秒数
     * @return 是否获取成功
     */
    boolean tryGetLock(String lockKey, String requestId, int expireMillisecond);

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁key
     * @param requestId (请求标识)加锁者标识
     * @return 是否释放成功
     */
    boolean releaseLock(String lockKey, String requestId);
}