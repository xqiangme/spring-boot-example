package
        com.example.redis;

import com.example.redis.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * Redis 分布式锁
 *
 * @author 码农猿
 */
@Component
public class RedisLock {

    private static final Long ONE = 1L;
    private static final String DEFAULT_LOCK_VALUE = "1";
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
     * @param lockKey    锁key
     * @param requestId  (请求标识)加锁者标识
     * @param expireTime 超期时间 毫秒数
     * @return 是否获取成功
     */
    public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        return redisClient.invoke(jedisPool, jedis -> {
                    String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                    if (OK.equals(result)) {
                        return true;
                    }
                    return false;
                }
        );
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁key
     * @param expireTime 超期时间 毫秒数
     * @return 是否获取成功
     */
    public boolean tryGetLock(String lockKey, int expireTime) {
        return tryGetLock(lockKey, DEFAULT_LOCK_VALUE, expireTime);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁key
     * @param requestId (请求标识)加锁者标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return redisClient.invoke(jedisPool, jedis -> {
                    Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
                    if (ONE.equals(result)) {
                        return true;
                    }
                    return false;
                }
        );
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁key
     * @return 是否释放成功
     */
    public void releaseLock(String lockKey) {
        redisClient.invoke(jedisPool, (jedis) -> {
            jedis.del(lockKey);
            return null;
        });
    }
}