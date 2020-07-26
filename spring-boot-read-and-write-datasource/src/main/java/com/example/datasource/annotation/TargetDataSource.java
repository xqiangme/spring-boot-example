package com.example.datasource.annotation;

import com.example.datasource.dynamic.DataSourceTypeEnum;

import java.lang.annotation.*;

/**
 * 目标数据源注解-作用于方法上
 *
 * @author 程序员小强
 * @date 2020-07-26
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {

    /**
     * 目标数据源枚举名称
     */
    DataSourceTypeEnum value();
}
