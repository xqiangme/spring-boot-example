package com.example.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Hash -数据类型-相关操作
 *
 * @author mengqiang
 */
public interface HashTemplateService {

    /**
     * 用于查看哈希表的指定字段是否存在
     *
     * @param key
     * @param field
     */
    Boolean hexists(String key, String field);

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
    Boolean hSet(String key, String field, Object value);

    Boolean hSet(String key, String field, Object value, int duration, TimeUnit timeUnit);

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
    Boolean hSetNx(String key, String field, Object value);


    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param field 属性名称
     * @return 执行命令之后 key 的值。
     */
    Long hashIncr(String key, String field);

    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    Long hashIncrBy(String key, String field, long value);

    /**
     * key 中HASH储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    Double hashIncrByFloat(String key, String field, Double value);

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null
     */
    <T> T hGet(String key, String field, Class<T> clazz);

    /**
     * 根据key集合批量获取
     *
     * @param key    key集合
     * @param fields field集合
     * @param classz 序列化对象
     * @return 类对象
     */
    <T> List<T> hmget(String key, List<String> fields, Class<T> classz);


    /**
     * 用于删除哈希表 key 中的个指定字段
     *
     * @param key
     * @param field
     * @return 被成功删除字段的数量，不包括被忽略的字段
     */
    Long hdel(String key, String field);

    /**
     * 获取hash 大小
     *
     * @param key
     */
    Long hlen(String key);

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     *
     * @param key
     * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
     */
    <T> Map<String, T> hgetAll(String key, Class<T> clazz);

    /**
     * 获取在哈希表中指定 key 的所有field的值
     *
     * @param key
     * @return 若 key 不存在，返回空列表。
     */
    <T> List<T> hvals(String key, Class<T> clazz);

    /**
     * 返回hash key 对应所有field
     *
     * @param key
     * @return 当 key 不存在时，返回一个空表。
     */
    Set<String> hkeys(String key);

    /**
     * 获取Hash下所有数据
     *
     * @param hashKey
     */
    Map<String, String> hgetall(String hashKey);

}