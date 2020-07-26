package com.example.datasource.aspect;

import com.example.datasource.dynamic.DynamicDataSourceContextHolder;
import com.example.datasource.annotation.TargetDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 目标数据源注解
 * 注：@Order(-1)是为了保证该AOP在@Transactional之前执行
 *
 * @author 程序员小强
 * @date 2020-07-26
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * 拦截使用 @TargetDataSource注解的方法，前置设置数据源
     *
     * @param point
     * @param ds
     */
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) {
        logger.info("[ set DataSource ] >> {} > {}", ds.value(), point.getSignature());
        DynamicDataSourceContextHolder.setDataSourceType(ds.value());
    }

    /**
     * 拦截使用 @TargetDataSource注解的方法，后置-移除数据源
     *
     * @param point
     * @param ds
     */
    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.info("[ remove DataSource ] >> {} > {}", ds.value(), point.getSignature());
        DynamicDataSourceContextHolder.removeDataSourceType();
    }
}
