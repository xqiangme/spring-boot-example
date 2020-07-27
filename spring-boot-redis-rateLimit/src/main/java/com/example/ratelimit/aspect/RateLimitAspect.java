package com.example.ratelimit.aspect;

import com.example.ratelimit.annotation.RateLimit;
import com.example.ratelimit.component.RedisRateLimitComponent;
import com.example.ratelimit.exception.RateLimitException;
import com.example.ratelimit.util.ReflectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Parameter;


/**
 * 描述:限流切面实现
 *
 * @author 程序员小强
 **/
@Aspect
@Configuration
public class RateLimitAspect {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    private RedisRateLimitComponent redisRateLimitComponent;

    @Autowired
    public RateLimitAspect(RedisRateLimitComponent redisRateLimitComponent) {
        this.redisRateLimitComponent = redisRateLimitComponent;
    }

    /**
     * 匹配所有使用以下注解的方法
     *
     * @see RateLimit
     */
    @Pointcut("@annotation(com.example.ratelimit.annotation.RateLimit)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(rateLimit)")
    public Object logAround(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        //组装限流key
        String rateLimitKey = this.getRateLimitKey(joinPoint, rateLimit);

        //限流组件-通过计数方式限流
        Long count = redisRateLimitComponent.rateLimit(rateLimitKey, rateLimit.time(), rateLimit.count());
        logger.debug("[ RateLimit ] method={},rateLimitKey={},count={}", methodName, rateLimitKey, count);

        if (null != count && count.intValue() <= rateLimit.count()) {
            //未超过限流次数-执行业务方法
            return joinPoint.proceed();
        } else {
            //超过限流次数
            logger.info("[ RateLimit ] >> over the max request times method={},rateLimitKey={},currentCount={},rateLimitCount={}",
                    methodName, rateLimitKey, count, rateLimit.count());
            throw new RateLimitException(rateLimit.msg());
        }
    }

    /**
     * 获取限流key
     * 默认取 RateLimit > key 属性值
     * 若设置了 keyField 则从参数中获取该字段的值拼接到key中
     * 示例：user_phone_login_max_times:13235777777
     *
     * @param joinPoint
     * @param rateLimit
     */
    private String getRateLimitKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        String fieldName = rateLimit.keyField();
        if ("".equals(fieldName)) {
            return rateLimit.key();
        }

        //处理自定义-参数名-动态属性key
        StringBuilder rateLimitKeyBuilder = new StringBuilder(rateLimit.key());
        //当前方法的参数
        for (Object obj : joinPoint.getArgs()) {
            if (null == obj) {
                continue;
            }
            //过滤基本类型参数
            if (ReflectionUtil.isJdkClazz(obj.getClass())) {
                continue;
            }
            //仅支持-自定义类参数
            //属性值
            Object fieldValue = ReflectionUtil.getFieldByClazz(fieldName, obj);
            if (null != fieldValue) {
                rateLimitKeyBuilder.append(":").append(fieldValue.toString());
                break;
            }
        }
        return rateLimitKeyBuilder.toString();
    }
}
