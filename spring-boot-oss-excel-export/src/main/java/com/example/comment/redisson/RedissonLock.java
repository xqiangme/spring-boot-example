package com.example.comment.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson的分布式锁
 */
@Component
public class RedissonLock {

  /**
   * 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

  /**
   * 默认缓存时间
   */
  private static final Long DEFAULT_EXPIRED = 300L;

  /**
   * redisson client对象
   */
  @Autowired
  private RedissonClient redissonClient;


  /**
   * 暴露redisson的Lock对象
   *
   * @param key
   * @return
   */
  public RLock getRedisLock(String key) {
    return redissonClient.getLock(key);
  }

  /**
   * 加锁
   * <p>
   * 如果锁不可用，则当前线程处于休眠状态，直到获得锁为止。
   *
   * @param lockKey 锁key
   */
  public void lock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock();
  }

  /**
   * 加锁，自定义超时时间
   *
   * @param lockKey        锁key
   * @param timeoutSeconds 超时秒数
   */
  public void lock(String lockKey, int timeoutSeconds) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(timeoutSeconds, TimeUnit.SECONDS);
  }

  /**
   * 加锁，自定义超时时间
   *
   * @param lockKey 锁key
   * @param timeout 超时时间数
   * @param unit    超时时间单位 示例 秒：TimeUnit.SECONDS
   */
  public void lock(String lockKey, int timeout, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(timeout, unit);
  }

  /**
   * 尝试加锁
   *
   * @param lockKey 锁key
   */
  public boolean tryLock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    return lock.tryLock();
  }

  /**
   * 尝试加锁，自定义等待时间，超时时间
   *
   * @param lockKey        锁key
   * @param timeoutSeconds 超时时间数
   */
  public boolean tryLock(String lockKey, long waitTime, long timeoutSeconds) {
    return tryLock(lockKey, waitTime, timeoutSeconds, TimeUnit.SECONDS);
  }

  /**
   * 尝试加锁，自定义等待时间，默认超时时间5分钟
   *
   * @param lockKey 锁key
   */
  public boolean tryLockByWaitTime(String lockKey, long waitTime) {
    return tryLock(lockKey, waitTime, DEFAULT_EXPIRED, TimeUnit.SECONDS);
  }

  /**
   * 尝试加锁，自定义等待时间，超时时间
   *
   * @param lockKey 锁key
   * @param timeout 超时时间数
   * @param unit    超时时间单位 示例 秒：TimeUnit.SECONDS
   */
  public boolean tryLock(String lockKey, long waitTime, long timeout, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      return lock.tryLock(waitTime, timeout, unit);
    } catch (InterruptedException e) {
      LOGGER.error("[ RedissonLock >> tryLock ] 加锁异常 stack:", e);
      return false;
    }
  }

  /**
   * 是否被锁定
   * 注：被任意一个线程锁持有
   *
   * @param lockKey
   * @return
   */
  public boolean isLocked(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    return lock.isLocked();
  }

  /**
   * 释放锁
   *
   * @param lockKey 锁key
   */
  public void unlock(String lockKey) {
    try {
      RLock lock = redissonClient.getLock(lockKey);
      lock.unlock();
    } catch (Exception e) {
      LOGGER.error("[ RedissonLock >> unlock ] 解锁异常 stack:", e);
    }
  }

}
