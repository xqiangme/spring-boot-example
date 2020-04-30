package com.example.redis.service.impl;

import com.example.redis.service.ListTemplateService;
import com.example.util.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.BinaryClient;

import java.util.ArrayList;
import java.util.List;

/**
 * List(集合) -数据类型-相关操作
 *
 * @author mengqiang
 */
@Component
public class ListTemplateServiceImpl extends BaseTemplateImpl implements ListTemplateService {

    /**
     * Redis Rpush 命令用于将一个或多个值插入到列表的尾部(最右边)。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     * <p>
     * 注意：在 Redis 2.4 版本以前的 RPUSH 命令，都只接受单个 value 值。
     *
     * @param key
     * @return 列表的长度。
     */
    @Override
    public Long lRpushObject(String key, Object... value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.rpush(key.getBytes(), JsonSerializer.serialize(value));
        });
    }


    /**
     * 移除列表元素
     * 移除列表中与参数 VALUE 相等的元素
     *
     * @param key
     * @return 被移除元素的数量。 列表不存在时返回 0
     * 根据count值,从列表中删除所有value相等的项
     * (1) count>0 , 删除从左到右,最多count个value相等的项
     * (2) count<0 ,删除从右到左,最多count绝对值个value相等的项
     * (3) count=0 ,删除所有value相等的项
     */
    @Override
    public Long lRem(String key, Object value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.lrem(key.getBytes(), ZERO, JsonSerializer.serialize(value));
        });
    }


    /**
     * 将一个或多个值插入到列表头部
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
     * 当 key 存在但不是列表类型时，返回一个错误
     * 注意：在Redis 2.4版本以前的 lpush 命令，都只接受单个 value 值
     *
     * @param key
     * @return 列表的长度。
     */
    @Override
    public Long lPush(String key, Object value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.lpush(key.getBytes(), JsonSerializer.serialize(value));
        });
    }

    /**
     * 通过索引来设置元素的值。
     * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
     * 操作成功返回 ok ，否则返回错误信息。
     *
     * @param key
     * @return 操作是否成功
     */
    @Override
    public Boolean lset(String key, long index, Object value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            String result = jedis.lset(key.getBytes(), index, JsonSerializer.serialize(value));
            return this.isStringEquals(OK, result);
        });
    }

    /**
     * 将一个或多个值插入到列表尾部
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
     * 当 key 存在但不是列表类型时，返回一个错误
     *
     * @param key
     * @return 列表的长度。
     */
    @Override
    public Long rpush(String key, Object value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.rpush(key.getBytes(), JsonSerializer.serialize(value));
        });
    }

    /**
     * 在list指定的值前插入newValue
     */
    @Override
    public Long linSertBefore(String key, Object value, Object newValue) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.linsert(key.getBytes(),
                    BinaryClient.LIST_POSITION.BEFORE,
                    JsonSerializer.serialize(value),
                    JsonSerializer.serialize(newValue));
        });
    }

    /**
     * 在list指定的值后插入newValue
     */
    @Override
    public Long linSertAfter(String key, Object value, Object newValue) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.linsert(key.getBytes(),
                    BinaryClient.LIST_POSITION.AFTER,
                    JsonSerializer.serialize(value),
                    JsonSerializer.serialize(newValue));
        });
    }

    /**
     * 对一个列表进行修剪(trim)
     * 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     *
     * @param key
     * @param start 保留区间开始下标
     * @param end   保留区间结束下标
     * @return 是否成功
     */
    @Override
    public Boolean lTrim(String key, long start, long end) {
        validateKeyParam(key);
        String result = redisClient.invoke(jedisPool, jedis -> {
            return jedis.ltrim(key.getBytes(), start, end);
        });
        //是否成功
        return this.isStringEquals(OK, result);
    }

    /**
     * 用于返回列表的长度
     * 如果列表 key 不存在，则 key 被解释为一个空列表，
     * 返回 0 。 如果 key 不是列表类型，返回一个错误
     *
     * @param key
     * @return list 集大小
     */
    @Override
    public long lLen(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.llen(key.getBytes());
        });
    }


    /**
     * 通过索引获取列表中的元素
     * 如果指定索引值不在列表的区间范围内，返回 null
     * 使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param index 集合索引
     * @return 元素信息
     */
    @Override
    public <T> T lIndex(String key, int index, Class<T> clazz) {
        validateParam(key, clazz);
        T obj = redisClient.invoke(jedisPool, jedis -> {
            byte[] result = jedis.lindex(key.getBytes(), index);
            if (result == null) {
                return null;
            }
            return JsonSerializer.deserialize(result, clazz);
        });
        return obj;
    }

    /**
     * 返回列表中指定区间内的字符串 元素
     * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素
     * 可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 相应对象集合
     */
    @Override
    public List<String> lRange(String key, int start, int end) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            List<String> strList = jedis.lrange(key, start, end);
            if (CollectionUtils.isEmpty(strList)) {
                return new ArrayList<>(ZERO);
            }
            return strList;
        });

    }


    /**
     * 返回列表中指定区间内的元素
     * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素
     * 可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 相应对象集合
     */
    @Override
    public <T> List<T> lRange(String key, Class<T> clazz, int start, int end) {
        validateKeyParam(key);
        List<T> result = redisClient.invoke(jedisPool, jedis -> {
            List<byte[]> set = jedis.lrange(key.getBytes(), start, end);
            List<T> list = new ArrayList<>(set.size());
            if (CollectionUtils.isEmpty(set)) {
                return new ArrayList<T>(ZERO);
            }
            for (byte[] bytes : set) {
                T t = JsonSerializer.deserialize(bytes, clazz);
                list.add(t);
            }
            return list;
        });
        return result;
    }

    /**
     * 移除并返回列表的第一个元素
     *
     * @param key
     * @return 列表的第一个元素。 当列表 key 不存在时，返回 null
     */
    @Override
    public <T> T lPop(String key, Class<T> clazz) {
        validateParam(key, clazz);
        return redisClient.invoke(jedisPool, jedis -> {
            byte[] result = jedis.lpop(key.getBytes());
            if (result == null) {
                return null;
            }
            return JsonSerializer.deserialize(result, clazz);
        });
    }

    /**
     * 移除并返回列表的最后一个元素
     *
     * @param key
     * @return 列表的最后一个元素。 当列表不存在时，返回 null
     */
    @Override
    public <T> T rPop(String key, Class<T> clazz) {
        validateParam(key, clazz);
        return redisClient.invoke(jedisPool, jedis -> {
            byte[] result = jedis.rpop(key.getBytes());
            if (result == null) {
                return null;
            }
            return JsonSerializer.deserialize(result, clazz);
        });
    }
}