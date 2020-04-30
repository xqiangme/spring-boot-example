package com.example.redis.service.impl;

import com.example.redis.service.HashTemplateService;
import com.example.util.JsonSerializer;
import com.example.util.TimeUnitUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Hash -数据类型-相关操作
 *
 * @author mengqiang
 */
@Component
public class HashTemplateServiceImpl extends BaseTemplateImpl implements HashTemplateService {

    /**
     * 用于查看哈希表的指定字段是否存在
     *
     * @param key
     * @param field
     */
    @Override
    public Boolean hexists(String key, String field) {
        validateParam(key, field);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.hexists(key.getBytes(), field.getBytes()));
    }

    /**
     * 用于为哈希表中的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key
     * @param field
     * @param value 值
     *              注:仅在field不存在，并成功添加时返回 true,
     *              field已经存在，会更新内容，但添加状态是false
     */
    @Override
    public Boolean hSet(String key, String field, Object value) {
        validateKeyParam(key);
        Long result = redisClient.invoke(jedisPool, (jedis) ->
                jedis.hset(key.getBytes(), field.getBytes(),
                        JsonSerializer.serialize(value))
        );
        return this.isLongEquals(LONG_ONE, result);
    }

    @Override
    public Boolean hSet(String key, String field, Object value, int duration, TimeUnit timeUnit) {
        validateKeyParam(key);
        Long result = redisClient.invoke(jedisPool, (jedis) -> {
                    Long hashResult = jedis.hset(key.getBytes(), field.getBytes(),
                            JsonSerializer.serialize(value));
                    jedis.pexpire(key.getBytes(), TimeUnitUtil.getMillis(timeUnit, duration));
                    return hashResult;
                }
        );
        return this.isLongEquals(LONG_ONE, result);
    }


    /**
     * 用于为哈希表中不存在的的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，操作无效
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
     * 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
     * 注意版本 >= 2.0.0
     *
     * @param key
     * @param field
     * @param value 值
     * @return 是否成功
     */
    @Override
    public Boolean hSetNx(String key, String field, Object value) {
        validateKeyParam(key);
        Long result = redisClient.invoke(jedisPool, (jedis) ->
                jedis.hsetnx(key.getBytes(), field.getBytes(),
                        JsonSerializer.serialize(value)));
        return this.isLongEquals(LONG_ONE, result);
    }


    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param field 属性名称
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Long hashIncr(String key, String field) {
        return this.hashIncrBy(key, field, 1L);
    }

    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Long hashIncrBy(String key, String field, long value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) ->
                jedis.hincrBy(key.getBytes(), field.getBytes(), value));
    }

    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    @Override
    public Double hashIncrByFloat(String key, String field, Double value) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) ->
                jedis.hincrByFloat(key.getBytes(), field.getBytes(), value));
    }

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null
     */
    @Override
    public <T> T hGet(String key, String field, Class<T> clazz) {
        validateKeyParam(key);
        byte[] value = redisClient.invoke(jedisPool, (jedis) -> jedis.hget(key.getBytes(), field.getBytes()));
        if (value != null) {
            return JsonSerializer.deserialize(value, clazz);
        }
        return null;
    }

    /**
     * 根据key集合批量获取
     *
     * @param key    key集合
     * @param fields field集合
     * @param classz 序列化对象
     * @return 类对象
     */
    @Override
    public <T> List<T> hmget(String key, List<String> fields, Class<T> classz) {
        if (CollectionUtils.isEmpty(fields)) {
            return Lists.newArrayList();
        }
        String[] strings = new String[fields.size()];
        fields.toArray(strings);

        List<T> result = redisClient.invoke(jedisPool, jedis -> {
            List<String> list = jedis.hmget(key, strings);

            List<T> resultList = Lists.newArrayList();
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<T>();
            }
            for (String strValue : list) {
                if (StringUtils.isNoneBlank(strValue)) {
                    T t = JsonSerializer.deserialize(strValue.getBytes(), classz);
                    resultList.add(t);
                }
            }

            return resultList;
        });
        return result;
    }


    /**
     * 用于删除哈希表 key 中的个指定字段
     *
     * @param key
     * @param field
     * @return 被成功删除字段的数量，不包括被忽略的字段
     */
    @Override
    public Long hdel(String key, String field) {
        validateParam(key, field);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.hdel(key.getBytes(), field.getBytes()));
    }


    /**
     * 获取hash 大小
     *
     * @param key
     */
    @Override
    public Long hlen(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> jedis.hlen(key.getBytes()));
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     *
     * @param key
     * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
     */
    @Override
    public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> {
            Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());
            Map<String, T> resultMap = Maps.newHashMap();
            if (map != null) {
                for (Map.Entry<byte[], byte[]> item : map.entrySet()) {
                    byte[] newKey = item.getKey();
                    T newValue = JsonSerializer.deserialize(item.getValue(), clazz);
                    resultMap.put(new String(newKey), newValue);
                }
                return resultMap;
            }
            return null;
        });
    }

    /**
     * 获取在哈希表中指定 key 的所有field的值
     *
     * @param key
     * @return 若 key 不存在，返回空列表。
     */
    @Override
    public <T> List<T> hvals(String key, Class<T> clazz) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> {
            List<byte[]> byteList = jedis.hvals(key.getBytes());
            List<T> list = new ArrayList<>(byteList.size());
            if (CollectionUtils.isEmpty(byteList)) {
                return new ArrayList<T>(ZERO);
            }
            for (byte[] bytes : byteList) {
                T t = JsonSerializer.deserialize(bytes, clazz);
                list.add(t);
            }
            return list;
        });
    }

    /**
     * 返回hash key 对应所有field
     *
     * @param key
     * @return 当 key 不存在时，返回一个空表。
     */
    @Override
    public Set<String> hkeys(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, (jedis) -> {
            return jedis.hkeys(key);
        });
    }

    /**
     * 获取Hash下所有数据
     *
     * @param hashKey
     */
    @Override
    public Map<String, String> hgetall(String hashKey) {
        Map<String, String> result = redisClient.invoke(jedisPool, jedis -> jedis.hgetAll(hashKey));
        return result;
    }

}