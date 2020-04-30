package com.example.redis.service;

import java.util.List;
import java.util.Set;


/**
 * Set 数据类型 -相关操作
 *
 * @author mengqiang
 */
public interface SetTemplateService {

    /**
     * 向集合添加元素
     * 被添加到集合中的新元素的数量，不包括被忽略的元素。
     *
     * @param key
     */
    Long sAdd(String key, Object value);

    /**
     * 移除集合中元素
     * 被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key
     */
    Long sRem(String key, Object value);

    /**
     * 获取集合的成员数
     * 被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key
     * @return 集合的数量。 当集合 key 不存在时，返回 0 。
     */
    Long sCard(String key);

    /**
     * 判断 value 元素是否是集合 key 的成员
     * 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
     *
     * @param key
     */
    Boolean sisMember(String key, Object value);

    /**
     * 返回集合中的所有成员
     *
     * @param key
     */
    <T> Set<T> sMembers(String key, Class<T> clazz);

    /**
     * 返回集合随机count个值
     *
     * @param key
     */
    <T> Set<T> sRandMember(String key, Class<T> clazz, int count);


    /**
     * 返回给定集合的交集
     *
     * @param keys
     */
    <T> Set<T> sinter(Set<String> keys, Class<T> clazz);

    /**
     * 返回给定集合的并集
     *
     * @param keys
     */
    <T> Set<T> sunion(Set<String> keys, Class<T> clazz);

    /**
     * 返回给定集合的差集
     *
     * @param keys
     */
    <T> Set<T> sDiff(List<String> keys, Class<T> clazz);

    /**
     * 移除并返回集合中的一个随机元素
     *
     * @param key
     */
    <T> T sPop(String key, Class<T> clazz);
}