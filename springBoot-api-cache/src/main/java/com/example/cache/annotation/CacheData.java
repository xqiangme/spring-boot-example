package com.example.cache.annotation;

import com.example.aspect.CacheAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存注解
 *
 * @author mengqiang
 * @see CacheAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheData {

    /**
     * 自定义缓存key前缀
     * 默认：方法名
     */
    String keyPrefix() default "";

    /**
     * 缓存过期时间
     * 单位默认：秒,不设置默认10分钟
     */
    int expireTime() default 600;

    /**
     * 缓存过期-随机数
     * 单位默认：秒,默认0分钟
     * 注：设置后实际过期时间，
     * 会在expireTime基础上继续累积(0~randomExpire)之间的秒数,防止缓存大量失效大面积穿透，造成雪崩
     */
    int randomExpire() default 0;

    /**
     * 是否存储为null 的返回
     * 注：防止缓存穿透，默认true,建议查询为空时，也进行缓存
     */
    boolean storageNullFlag() default true;
}
