package com.example.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * Redis 键命令-key -相关操作
 *
 * @author mengqiang
 */
public interface KeyTemplateService {

    /**
     * 检查给定 key 是否存在
     *
     * @param key
     * @return 是否存在
     */
    Boolean exists(String key);

    /**
     * 用于设置 key 的过期时间，
     * key 过期后将不再可用。单位以秒计
     *
     * @param key
     * @param seconds
     * @return 是否设置成功
     */
    Boolean expire(String key, int seconds);

    /**
     * 用于设置 key 的过期时间，key 过期后将不再可用
     * 设置成功返回 1
     * 当 key 不存在或者不能为 key 设置过期时间时返回
     *
     * @param key
     * @param duration 时间量与单位一起使用
     * @param timeUnit 单位枚举类
     * @return
     */
    Boolean expire(String key, int duration, TimeUnit timeUnit);

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
    Long getExpiresTtl(String key);

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
    Long getExpiresPttl(String key);

    /**
     * 移除 key 的过期时间，key 将持久保持。
     * 当过期时间移除成功时，返回 1
     * 如果 key 不存在或 key 没有设置过期时间，返回 0
     *
     * @param key
     */
    Boolean persist(String key);

    /**
     * 根据key 获取存储类型
     *
     * @param key
     * @return 返回 key 的数据类型
     */
    String getType(String key);

    /**
     * 用于删除已存在的键。不存在的 key 会被忽略
     * 被删除 key 的数量
     *
     * @param key
     */
    Long del(String key);

}