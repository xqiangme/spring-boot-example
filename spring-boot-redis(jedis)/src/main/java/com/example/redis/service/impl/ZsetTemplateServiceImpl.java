package com.example.redis.service.impl;

import com.example.redis.model.RedisRangScoresModel;
import com.example.redis.service.ZsetTemplateService;
import com.example.util.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Zset(有些集合) 数据类型 - 相关操作
 *
 * @author mengqiang
 */
@Component
public class ZsetTemplateServiceImpl extends BaseTemplateImpl implements ZsetTemplateService {

    /**
     * 添加元素到有序集合,有序集合是按照元素的score进行排列
     * 注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param obj
     * @param score 分值
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
     */
    @Override
    public Long zAdd(String key, Object obj, double score) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zadd(key.getBytes(), score, JsonSerializer.serialize(obj));
        });
    }

    /**
     * 根据分值批量添加
     */
    @Override
    public void zAddWithMapScore(String key, Map<byte[], Double> members) {
        redisClient.invoke(jedisPool, jedis -> {
            jedis.zadd(key.getBytes(), members);
            return null;
        });
    }

    /**
     * 根据key 计算集合中元素的数量
     *
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
     */
    @Override
    public long zCard(String key) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zcard(key.getBytes());
        });
    }

    /**
     * 根据key 计算在有序集合中指定区间分数的成员数
     *
     * @param key
     * @param minScore 最小排序分值
     * @param maxScore 最大排序分值
     * @return 分数值在 min 和 max 之间的成员的数量。
     */
    @Override
    public Long zCount(String key, double minScore, double maxScore) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zcount(key.getBytes(), minScore, maxScore);
        });
    }

    /**
     * 返回有序集中，指定区间内的成员 -> 从小到大
     * 其中成员的位置按分数值递增(从小到大)来排序
     * <p>
     * 具有相同分数值的成员按字典序来排列
     * 注意：下标参数0 为起始
     * 负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 相应对象集合
     */
    @Override
    public <T> List<T> zRange(String key, Class<T> clazz, int start, int end) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            Set<byte[]> set = jedis.zrange(key.getBytes(), start, end);
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
    }

    /**
     * 返回有序集中，指定区间内的成员 -> 从大到小
     * 其中成员的位置按分数值递增(从大到小)来排序
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    @Override
    public <T> List<RedisRangScoresModel<T>> zRevRangeWithScores(String key, Class<T> clazz, int start, int end) {
        validateKeyParam(key);
        List<RedisRangScoresModel<T>> result = redisClient.invoke(jedisPool, jedis -> {
            Set<Tuple> set = jedis.zrevrangeWithScores(key.getBytes(), start, end);
            List<RedisRangScoresModel<T>> list = new ArrayList<>(set.size());
            if (CollectionUtils.isEmpty(set)) {
                return new ArrayList<RedisRangScoresModel<T>>();
            }
            RedisRangScoresModel<T> rangScores = null;
            for (Tuple bytes : set) {
                rangScores = new RedisRangScoresModel<T>();
                T t = JsonSerializer.deserialize(bytes.getBinaryElement(), clazz);
                rangScores.setObj(t);
                rangScores.setScore(bytes.getScore());
                list.add(rangScores);
            }
            return list;
        });
        return result;
    }

    /**
     * 返回有序集中，指定区间内的成员 -> 从大到小
     * 其中成员的位置按分数值递增(从大到小)来排序
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    @Override
    public <T> List<T> zRevRange(String key, Class<T> clazz, int start, int end) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            Set<byte[]> set = jedis.zrevrange(key.getBytes(), start, end);
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
    }

    /**
     * 通过分数返回有序集合指定区间内的成员 -> 从小到大
     * 有序集成员按分数值递增(从小到大)次序排列
     *
     * @param key
     * @param clazz
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public <T> List<T> zRangeByScore(String key, Class<T> clazz, double minScore, double maxScore) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            Set<byte[]> set = jedis.zrangeByScore(key.getBytes(), minScore, maxScore);
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
    }

    /**
     * 通过分数返回有序集合指定区间内的成员 -> 从大到小
     * 有序集成员按分数值递增(从大到小)次序排列
     *
     * @param key
     * @param clazz
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public <T> List<T> zRevRangeByScore(String key, Class<T> clazz, double minScore, double maxScore) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            Set<byte[]> set = jedis.zrevrangeByScore(key.getBytes(), maxScore, minScore);
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
    }


    /**
     * 返回有序集中指定成员的排名
     * 按分数值递增(从小到大)顺序排列
     * 排名以 0 为底，也就是说， 分数值最小的成员排名为 0
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。
     * 如果成员不是有序集 key 的成员，返回空
     */
    @Override
    public Long zRank(String key, Object obj) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zrank(key.getBytes(), JsonSerializer.serialize(obj));
        });
    }

    /**
     * 返回有序集中指定成员的排名
     * 分数值递减(从大到小)排序
     * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。
     * 如果成员不是有序集 key 的成员，返回空
     */
    @Override
    public Long zRevRank(String key, Object obj) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zrevrank(key.getBytes(), JsonSerializer.serialize(obj));
        });
    }

    /**
     * 移除有序集合中的个成员
     * 名称为key 的有序集合中的元素 obj
     *
     * @param key
     * @param obj 元素
     * @return 被成功移除的成员的数量，不包括被忽略的成员
     */
    @Override
    public Long zRem(String key, Object obj) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zrem(key.getBytes(), JsonSerializer.serialize(obj));
        });
    }

    /**
     * 移除有序集合中给定的排名区间的所有成员
     * 从排序小的开始删除
     *
     * @param start 开始位置 下标 0 开始
     * @param end   结束位置
     * @return 被移除成员的数量
     */
    @Override
    public Long zRemRangeByRank(String key, int start, int end) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zremrangeByRank(key.getBytes(), start, end);
        });
    }

    /**
     * 移除有序集合中给定的分数区间的所有成员
     * 从排序小的开始删除
     *
     * @param startScore 开始分值
     * @param endScore   结束分值
     * @return 被移除成员的数量
     */
    @Override
    public Long zRemRangeByScore(String key, double startScore, double endScore) {
        validateKeyParam(key);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zremrangeByScore(key.getBytes(), startScore, endScore);
        });
    }

    /**
     * 返回有序集中，成员的分数值
     * 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名
     * 如果成员不是有序集 key 的成员，返回空
     */
    @Override
    public Double zScore(String key, Object obj) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zscore(key.getBytes(), JsonSerializer.serialize(obj));
        });
    }


    @Override
    public Double zIncrBy(String key, Object obj, double score) {
        validateParam(key, obj);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.zincrby(key.getBytes(), score, JsonSerializer.serialize(obj));
        });
    }


}