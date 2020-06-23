package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述: 限流注解
 *
 * @author 程序员小强
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流唯一标示 key
     * 若同时使用 keyFiled 则当前 key作为前缀
     */
    String key();

    /**
     * 限流时间-单位:秒数
     * 默认 60s
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count();

    /**
     * 可作为限流key-参数中属性名
     * 示例：phone、userId等
     */
    String keyField() default "";

    /**
     * 基于IP
     */
    boolean ipFlag() default false;

    /**
     * 超过最大访问次数后的，提示内容
     */
    String msg() default "over the max request times please try again";

}
