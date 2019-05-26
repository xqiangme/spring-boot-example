package com.example.annotation;

import java.lang.annotation.*;

/**
 * 接口统一异常处理
 * 注：所有需要捕获异常的接口均需要在实现处添加该注解
 *
 * @author 码农猿
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiAnnotation {

    /**
     * 方法描述
     */
    String description() default "业务接口";

}
