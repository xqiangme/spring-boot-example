package com.example.redis.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存注解
 *
 * @author mengqiang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheData {

    /**
     * 缓存过期时间
     * 单位默认：秒,不设置默认10分钟
     *
     * @return
     * @see CacheAspectOld
     */
    int expireTime() default 600;

    /**
     * 是否存储为null 的返回
     * 注：防止缓存穿透，建议查询为空时，也进行缓存
     *
     * @return
     * @see CacheAspectOld
     */
    boolean storageNullFlag() default false;

    /**
     * 自定义缓存key前缀
     * 默认：方法名
     *
     * @return
     */
    String keyPrefix() default "";
}
