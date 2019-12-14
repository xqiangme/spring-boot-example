package com.example.aspect;

import com.example.annotation.RateLimit;
import com.example.redis.RedisCache;
import com.example.redis.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 描述:拦截器
 *
 * @author yanpenglei
 * @create 2018-08-16 15:33
 **/
@Aspect
@Configuration
public class LimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisCache redisCache;

    private final int TIME = 1000 * 60;
    private final int ONE = 1;

    /**
     * 匹配所有使用以下注解的方法
     *
     * @see RateLimit
     */
    @Pointcut("@annotation(com.example.annotation.RateLimit)")
    public void pointCut() {
    }


    @Around("pointCut()&&@annotation(rateLimit)")
    public Object logAround(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        logger.info("");
        logger.info("[{}] >>  方法 , 限流拦截执行开始", methodName);

        //功能锁
        String lockKey = rateLimit.key().concat(methodName);
        try {
            //加锁
            boolean lock = redisLock.tryGetLock(lockKey, TIME);
            if (!lock) {
                logger.info("[{}] >>  方法 , 限流拦截执行-功能锁加锁失败 lockKey : {}", methodName, lockKey);
                throw new RuntimeException("并发操作了。。。");
            }

            //计数器+1，若当前key不存在，则新增并返回 1
            Long incr = redisCache.incr(rateLimit.key());

            //若第一次初始化计数器 > 则设置失效时间
            if (ONE == incr.intValue()) {
                //设置redis的过期时间
                redisCache.expire(rateLimit.key(), rateLimit.time(), TimeUnit.SECONDS);
                logger.info("[{}] >>  方法 , 限流拦截 >> 初始化计数器,失效时间", methodName);
            }

            //判断是否超过最大访问次数
            if (incr.intValue() > rateLimit.count()) {
                logger.info("[{}] >>  方法 , 限流拦截 >> 第{}次 已经到设置限流次数", methodName, incr.intValue());
                throw new RuntimeException("已经到设置限流次数");
            }

            logger.info("[{}] >>  方法 , 限流拦截 >> 第{}次正常业务", methodName, incr.intValue());
            logger.info("[{}] >>  方法 , 限流拦截执行结束", methodName);

            return joinPoint.proceed();
        } finally {

            //解锁
            redisLock.releaseLock(lockKey);
            logger.info("[{}] >>  方法 , 限流拦截执行-功能锁解锁", methodName);
        }
    }


//    //查询当前值
//    String value = redisCache.get(rateLimit.key());
//    //判断当前值是否存在
//            if (StringUtils.isNotBlank(value)) {
//        int number = Integer.parseInt(value);
//        if (number < rateLimit.count()) {
//            Long incr = redisCache.incr(rateLimit.key());
//            //若刚初始化计数器 > 则更新失效时间
//            if (ONE == incr.intValue()) {
//                //设置redis的过期时间
//                redisCache.expire(rateLimit.key(), rateLimit.time(), TimeUnit.SECONDS);
//                logger.info("[{}] >>  方法 , 限流拦截 >> 初始化计数器,失效时间", methodName);
//            }
//            logger.info("[{}] >>  方法 , 限流拦截 >> 第{}次正常业务", methodName, incr.intValue());
//        } else {
//            logger.info("number = {} 超过 max= {}不处理业务 ", number, rateLimit.count());
//            throw new RuntimeException("已经到设置限流次数");
//        }
//    } else {
//        redisCache.set(rateLimit.key(), ONE, rateLimit.time(), TimeUnit.SECONDS);
//        logger.info("[{}] >>  方法 , 限流拦截 >> 初始化计数器 key={}", methodName, rateLimit.key());
//    }
}
