package com.example.redis.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * String(字符串) 数据类型 -相关操作
 *
 * @author mengqiang
 */
public interface StringTemplateService {


    /**
     * 添加数据到redis
     * 注意：设置默认过期时间  30 分钟
     *
     * @param key
     * @param value
     */
    Boolean set(String key, Object value);

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
    Boolean set(String key, Object value, int duration, TimeUnit timeUnit);

    /**
     * 添加数据到redis
     * 并设置永不过期
     * 注：一般使用较少，数据过大时尽量不要使用
     * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
     *
     * @param key
     * @param value
     */
    Boolean putNeverExpires(String key, Object value);

    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 类class
     * @return 类对象
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 根据key集合批量获取
     *
     * @param keys   key集合
     * @param classz 序列化对象
     * @return 类对象
     */
    <T> List<T> mget(List<String> keys, Class<T> classz);

    /**
     * 根据key 获取值
     * 返回 key 的值，如果 key 不存在时，返回 nil。
     * 如果 key 不是字符串类型，那么返回一个错误。
     *
     * @param key
     * @return String
     */
    String get(String key);

    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 集合泛型对象
     * @return 集合对象
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认值是时间戳 默认有效期是 5 分钟
     *
     * @param key
     * @return 设置成功返回 true 失败返回false
     */
    Boolean setNx(String key);

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 注意！！：默认有效期是 5 分钟
     *
     * @param key
     * @param value 自定义值
     * @return 设置成功返回 true 失败返回false
     */
    Boolean setNx(String key, Object value);

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认值是时间戳
     *
     * @param key
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    Boolean setNx(String key, int seconds);

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认时间单位是秒
     *
     * @param key
     * @param value   自定义 value
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    Boolean setNx(String key, Object value, int seconds);

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
    Boolean setNx(String key, Object value, int duration, TimeUnit timeUnit);

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     * 注意！！：默认有效期为 5分钟
     *
     * @param key
     * @return String
     */
    String getSet(String key, String value);

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     *
     * @param key
     * @return string key 的旧值
     */
    String getSet(String key, String value, int duration, TimeUnit timeUnit);

    /**
     * 用于获取指定 key 所储存的字符串值的长度
     * 当 key 储存的不是字符串值时，返回一个错误
     * 当 key 不存在时，返回 0
     *
     * @param key
     */
    Long getStrLen(String key);

    /**
     * key 中储存的数字值增一 (默认增量+1)
     *
     * @param key
     * @return 执行命令之后 key 的值。
     */
    Long incr(String key);

    /**
     * key 中储存的数字值增一 （自定义增量值 ）
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    Long incrBy(String key, long value);

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值
     * 如果 key 不存在，那么 incrbyfloat 会先将 key 的值设为 0 ，再执行加法操作
     *
     * @param key
     * @param value 增量值
     * @return 执行命令之后 key 的值
     */
    Double incrByFloat(String key, Double value);

    /**
     * 将 key 中储存的数字值减一
     *
     * @param key
     * @return 执行命令之后 key 的值
     */
    Long decr(String key);

    /**
     * 将 key 中储存的数字值减自定义减量
     *
     * @param key
     * @param value 自定义减量值
     * @return 执行命令之后 key 的值。
     */
    Long decrBy(String key, long value);

    /**
     * 用于为指定的 key 追加值
     *
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度
     */
    Long append(String key, Object value);

}