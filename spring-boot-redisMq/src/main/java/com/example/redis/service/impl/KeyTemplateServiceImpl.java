package com.example.redis.service.impl;

import com.example.redis.service.KeyTemplateService;
import com.example.util.TimeUnitUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 键命令-key -相关操作
 *
 * @author mengqiang
 */
@Component
public class KeyTemplateServiceImpl extends BaseTemplateImpl implements KeyTemplateService {

    /**
     * 检查给定 key 是否存在
     *
     * @param key
     * @return 是否存在
     */
    @Override
    public Boolean exists(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.exists(key.getBytes()));
    }

    /**
     * 用于设置 key 的过期时间，
     * key 过期后将不再可用。单位以秒计
     *
     * @param key
     * @param seconds
     * @return 是否设置成功
     */
    @Override
    public Boolean expire(String key, int seconds) {
        return this.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 用于设置 key 的过期时间，key 过期后将不再可用
     * 设置成功返回 1
     * 当 key 不存在或者不能为 key 设置过期时间时返回 0
     * <p>
     * 时间枚举介绍
     * TimeUnit.DAYS          //天
     * TimeUnit.HOURS         //小时
     * TimeUnit.MINUTES       //分钟
     * TimeUnit.SECONDS       //秒
     * TimeUnit.MILLISECONDS  //毫秒
     * TimeUnit.NANOSECONDS   //毫微秒
     * TimeUnit.MICROSECONDS  //微秒
     *
     * @param key
     * @param duration 时间量与单位一起使用
     * @param timeUnit 单位枚举类
     * @return
     */
    @Override
    public Boolean expire(String key, int duration, TimeUnit timeUnit) {
        validateKeyParam(key);
        //时间转换成毫秒
        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
        Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.pexpire(key.getBytes(), millis));
        return this.isLongEquals(LONG_ONE, lResult);
    }

    /**
     * 根据key 获取过期时间秒数
     * 不存在时返回负数
     *
     * @param key
     * @return 剩余过期时间秒数
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1
     * 否则，以秒为单位，返回 key 的剩余生存时间
     */
    @Override
    public Long getExpiresTtl(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.ttl(key.getBytes()));
    }

    /**
     * 根据key 获取过期时间毫秒数
     * 不存在时返回负数
     *
     * @param key
     * @return 剩余过期时间毫秒数
     * 当 key 不存在时，返回 -2
     * 当 key 存在但没有设置剩余生存时间时，返回 -1
     * 否则，以毫秒为单位，返回 key 的剩余生存时间
     */
    @Override
    public Long getExpiresPttl(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.pttl(key.getBytes()));
    }

    /**
     * 移除 key 的过期时间，key 将持久保持。
     * 当过期时间移除成功时，返回 1
     * 如果 key 不存在或 key 没有设置过期时间，返回 0
     *
     * @param key
     */
    @Override
    public Boolean persist(String key) {
        validateKeyParam(key);
        Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.persist(key.getBytes()));
        return this.isLongEquals(LONG_ONE, lResult);
    }

    /**
     * 根据key 获取存储类型
     *
     * @param key
     * @return 返回 key 的数据类型
     * 数据类型有：
     * none (key不存在)
     * string (字符串)
     * list (列表)
     * set (集合)
     * zset (有序集)
     * hash (哈希表)
     */
    @Override
    public String getType(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.type(key.getBytes()));
    }

    /**
     * 用于删除已存在的键。不存在的 key 会被忽略
     * 被删除 key 的数量
     *
     * @param key
     */
    @Override
    public Long del(String key) {
        validateKeyParam(key);
        if (exists(key)) {
            return redisClient.invoke(jedisPool, (jedis) -> jedis.del(key.getBytes()));
        }
        return LONG_ZERO;
    }

    /**
     * 查找所有符合给定模式( pattern)的 key 。
     * 谨慎使用(存在性能问题)
     * 会引发Redis锁，并且增加Redis的CPU占用
     *
     * @param pattern
     * @return 符合给定模式的 key 列表 (Array)。
     */
    public List<String> findKeys(String pattern) {
        Assert.hasText(pattern, "查找规则不能为空");
        Charset charset = Charset.forName("UTF-8");
        return redisClient.invoke(jedisPool, jedis -> {
            Set<String> sets = jedis.keys(("*" + pattern + "*"));
            if (sets != null) {
                List<String> list = new ArrayList<>(sets.size());
                list.addAll(sets);
                return list;
            }
            return null;
        });
    }

}