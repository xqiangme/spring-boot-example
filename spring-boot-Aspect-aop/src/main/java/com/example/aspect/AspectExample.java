package com.example.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 切面测试
 *
 * @author 码农猿
 */
@Aspect
@Component
@Slf4j
public class AspectExample {

    /**
     * [@Pointcut]：统一切点,对com.example.controller及其子包中所有的类的所有方法切面
     * ps: @Pointcut("execution(public * com.example.controller..*.*(..))")
     */
    @Pointcut("execution(public * com.example.controller.AspectExampleController.*(..))")
    public void pointcut() {
        log.info("[切面处理] >> 使用注解 @Pointcut 定义切点位置");
    }

    /**
     * [@Before]：前置通知
     */
    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info("[切面处理] >> 使用注解 @Before 调用了方法前置通知 ");

    }

    /**
     * [@After]：后置通知
     */
    @After("pointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        log.info("[切面处理] >> 使用注解 @After 调用了方法后置通知 ");
    }

    /**
     * [@AfterRunning]：@AfterRunning: 返回通知 rsult为返回内容
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        log.info("[切面处理] >> 使用注解 @AfterReturning 调用了方法返回后通知 ");
    }

    /**
     * [@AfterThrowing]：异常通知
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception e) {
        log.info("[切面处理] >> 使用注解 @AfterThrowing 调用了方法异常通知 ");
    }


    /**
     * [@Around]：环绕通知
     */
    @Around("pointcut()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        //获取方法名称
        String methodName = pjp.getSignature().getName();
        log.info("[切面处理] >> 使用注解 @Around  方法 ：{} 执行之前 ", methodName);
        Object result = pjp.proceed();
        log.info("[切面处理] >> 使用注解 @Around  方法 ：{} 执行之后 ,返回值:{}", methodName, JSON.toJSONString(result));
        return result;
    }
}
