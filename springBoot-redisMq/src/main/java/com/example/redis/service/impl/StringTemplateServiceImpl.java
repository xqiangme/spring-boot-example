package com.example.redis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.example.redis.service.StringTemplateService;
import com.example.util.JsonSerializer;
import com.example.util.SystemClock;
import com.example.util.TimeUnitUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * String(字符串) 数据类型 -相关操作
 *
 * @author mengqiang
 */
@Component
public class StringTemplateServiceImpl extends BaseTemplateImpl implements StringTemplateService {

    /**
     * 添加数据到redis
     * 设置默认过期时间  30 分钟
     *
     * @param key
     * @param value
     */
    @Override
    public Boolean set(String key, Object value) {
        return set(key, value, THIRTY, TimeUnit.MINUTES);
    }

    /**
     * 添加数据到redis
     * 自定义过期时间
     * 注：从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
     *
     * @param key
     * @param value
     * @param duration 时间量
     * @param timeUnit 时间单位枚举
     */
    @Override
    public Boolean set(String key, Object value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
        String result = redisClient.invoke(jedisPool, (jedis) -> {
                    String srtResult = jedis.set(key.getBytes(), JsonSerializer.serialize(value));
                    if (duration <= ZERO) {
                        //默认5 分钟
                        jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
                    } else {
                        //时间转换成毫秒
                        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
                        jedis.pexpire(key.getBytes(), millis);
                    }
                    return srtResult;
                }

        );
        //是否成功
        return this.isStringEquals(OK, result);
    }

    /**
     * 添加数据到redis
     * 并设置永不过期
     * 注：一般使用较少，数据过大时尽量不要使用
     * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
     *
     * @param key
     * @param value
     */
    @Override
    public Boolean putNeverExpires(String key, Object value) {
        validateParam(key, value);
        String result = redisClient.invoke(jedisPool, (jedis) -> {
                    return jedis.set(key.getBytes(), JsonSerializer.serialize(value));
                }
        );
        //是否成功
        return this.isStringEquals(OK, result);
    }

    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 类class
     * @return 类对象
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        validateKeyParam(key);
        byte[] result = redisClient.invoke(jedisPool, (jedis) -> jedis.get(key.getBytes()));
        if (result == null) {
            return null;
        }
        return JsonSerializer.deserialize(result, clazz);
    }

    /**
     * 根据key集合批量获取
     *
     * @param keys   key集合
     * @param classz 序列化对象
     * @return 类对象
     */
    @Override
    public <T> List<T> mget(List<String> keys, Class<T> classz) {
        if (CollectionUtils.isEmpty(keys)) {
            return Lists.newArrayList();
        }
        String[] strings = new String[keys.size()];
        keys.toArray(strings);
        List<T> result = redisClient.invoke(jedisPool, jedis -> {
            List<String> list = jedis.mget(strings);
            List<T> resultList = Lists.newArrayList();
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<T>();
            }
            for (String str : list) {
                T t = JsonSerializer.deserialize(str.getBytes(), classz);
                resultList.add(t);
            }
            return resultList;
        });
        return result;
    }


    /**
     * 根据key 获取值
     * 返回 key 的值，如果 key 不存在时，返回 nil。
     * 如果 key 不是字符串类型，那么返回一个错误。
     *
     * @param key
     * @return String
     */
    @Override
    public String get(String key) {
        return this.get(key, String.class);
    }

    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 集合泛型对象
     * @return 集合对象
     */
    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        String str = this.get(key, String.class);
        return JSONArray.parseArray(str, clazz);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认值是时间戳 默认有效期是 5 分钟
     *
     * @param key
     * @return 设置成功返回 true 失败返回false
     */
    @Override
    public Boolean setNx(String key) {
        return this.setNx(key, SystemClock.millisClock().now(), FIVE, TimeUnit.MINUTES);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认有效期是 5 分钟
     *
     * @param key
     * @param value 自定义值
     * @return 设置成功返回 true 失败返回false
     */
    @Override
    public Boolean setNx(String key, Object value) {
        return this.setNx(key, value, FIVE, TimeUnit.MINUTES);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认值是时间戳
     *
     * @param key
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    @Override
    public Boolean setNx(String key, int seconds) {
        return this.setNx(key, SystemClock.millisClock().now(), seconds, TimeUnit.SECONDS);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认时间单位是秒
     *
     * @param key
     * @param value   自定义 value
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    @Override
    public Boolean setNx(String key, Object value, int seconds) {
        return this.setNx(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 注：常用与分布式锁
     *
     * @param key
     * @param value
     * @param duration 时间量
     * @param timeUnit 时间单位枚举
     * @return 设置成功返回 true 失败返回false
     */
    @Override
    public Boolean setNx(String key, Object value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, (jedis) -> {
                    long result = jedis.setnx(key.getBytes(), JsonSerializer.serialize(value));
                    if (result >= 1) {
                        if (duration <= ZERO) {
                            //默认5 分钟
                            jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
                            return true;
                        } else {
                            //时间转换成毫秒
                            long millis = TimeUnitUtil.getMillis(timeUnit, duration);
                            jedis.pexpire(key.getBytes(), millis);
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
        );
    }

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     * 注：默认有效期为 5分钟
     *
     * @param key
     * @return String
     */
    @Override
    public String getSet(String key, String value) {
        return this.getSet(key, value, FIVE, TimeUnit.MINUTES);
    }

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     *
     * @param key
     * @return string key 的旧值
     */
    @Override
    public String getSet(String key, String value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, (jedis) -> {
                    String result = jedis.getSet(key, value);
                    if (duration <= ZERO) {
                        //设置默认过期时间 5 分钟
                        jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
                        return result;
                    } else {
                        //时间转换成毫秒
                        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
                        jedis.pexpire(key.getBytes(), millis);
                        return result;
                    }
                }
        );
    }

    /**
     * 用于获取指定 key 所储存的字符串值的长度
     * 当 key 储存的不是字符串值时，返回一个错误
     * 当 key 不存在时，返回 0
     *
     * @param key
     */
    @Override
    public Long getStrLen(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.strlen(key.getBytes()));
    }

    /**
     * key 中储存的数字值增一 (默认增量+1)
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     * 注：
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Long incr(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.incr(key.getBytes()));
    }

    /**
     * key 中储存的数字值增一 （自定义增量值 ）
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     * 注：
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Long incrBy(String key, long value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.incrBy(key.getBytes(), value));
    }

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值
     * 如果 key 不存在，那么 incrbyfloat 会先将 key 的值设为 0 ，再执行加法操作
     *
     * @param key
     * @param value 增量值
     * @return 执行命令之后 key 的值
     */
    @Override
    public Double incrByFloat(String key, Double value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.incrByFloat(key.getBytes(), value));
    }

    /**
     * 将 key 中储存的数字值减一
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return 执行命令之后 key 的值
     */
    @Override
    public Long decr(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.decr(key.getBytes()));
    }

    /**
     * 将 key 中储存的数字值减自定义减量
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param value 自定义减量值
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Long decrBy(String key, long value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.decrBy(key.getBytes(), value));
    }

    /**
     * 用于为指定的 key 追加值
     * <p>
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value
     * 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value
     * 就像执行 SET key value 一样
     *
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度
     */
    @Override
    public Long append(String key, Object value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.append(key.getBytes(), JsonSerializer.serialize(value)));
    }


}