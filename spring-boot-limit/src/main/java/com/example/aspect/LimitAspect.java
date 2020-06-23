package com.example.aspect;

import com.example.annotation.RateLimit;
import com.example.exception.RateLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;


/**
 * 描述:拦截器
 *
 * @author 程序员小强
 **/
@Aspect
@Configuration
public class LimitAspect {
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    @Autowired
    private JedisPool jedisPool;

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
        //限流key
        String rateLimitKey = rateLimit.key() + "." + methodName;
        logger.info("[ RateLimit ] >> start times method:{} , rateLimitKey = {}", methodName, rateLimitKey);

        //lua表达式
        String luaScript = this.buildLuaScript();
        //执行lua表达式通过计数限流
        Long count = this.executeLua(String.valueOf(luaScript), rateLimitKey, rateLimit);
        if (count != null && count.intValue() <= rateLimit.count()) {
            //未超过限流次数
            return joinPoint.proceed();
        } else {
            //超过限流次数
            logger.info("[ RateLimit ] >> over the max request times method={},rateLimitKey={},currentCount={},rateLimitCount={}",
                    methodName, rateLimitKey, count, rateLimit.count());
            throw new RateLimitException(rateLimit.msg());
        }
    }

    private String getRateLimitKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        String fieldName = rateLimit.keyField();
        if ("".equals(fieldName)) {
            return rateLimit.key();
        }
        //处理自定义属性
        StringBuilder rateLimitKeyBuilder = new StringBuilder(rateLimit.key());
        for (Object obj : joinPoint.getArgs()) {
            if (null == obj) {
                continue;
            }
            if (obj.getClass().isPrimitive()) {
                continue;
            }
            //属性值
            Object fieldValue = this.getFieldByClazz(fieldName, obj);
            if (null != fieldValue) {
                rateLimitKeyBuilder.append(":").append(fieldValue.toString());
                break;
            }
        }
        return rateLimitKeyBuilder.toString();
    }

    /**
     * 根据属性名获取属性元素，包括各种安全范围和所有父类
     *
     * @param fieldName
     * @param object
     * @return
     */
    private Object getFieldByClazz(String fieldName, Object object) {
        Field field = null;
        Class<?> clazz = object.getClass();
        try {
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                field = clazz.getDeclaredField(fieldName);
            }
            return field.get(object);
        } catch (Exception e) {
            // 这里甚么都不能抛出去。
            // 如果这里的异常打印或者往外抛，则就不会进入
        }
        return null;
    }

    /**
     * 执行 lua 表达式
     *
     * @param luaScript
     * @param key
     * @param rateLimit
     */
    public Long executeLua(String luaScript, String key, RateLimit rateLimit) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object obj = jedis.evalsha(jedis.scriptLoad(luaScript), Collections.singletonList(key),
                    Arrays.asList(String.valueOf(rateLimit.count()), String.valueOf(rateLimit.time())));
            return Long.valueOf(obj.toString());
        } catch (JedisException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
        return 0L;
    }

    /**
     * 构建lua 表达式
     * KEYS[1] -- 参数key
     * ARGV[1]-- 最大限流数
     * ARGV[2]-- 失效时间|秒
     */
    public String buildLuaScript() {
        StringBuilder luaBuilder = new StringBuilder();
        //定义变量
        luaBuilder.append("local count");
        //获取调用脚本时传入的第一个key值（用作限流的 key）
        luaBuilder.append("\ncount = redis.call('get',KEYS[1])");
        // 获取调用脚本时传入的第一个参数值（限流大小）-- 调用不超过最大值，则直接返回
        luaBuilder.append("\nif count and tonumber(count) > tonumber(ARGV[1]) then");
        luaBuilder.append("\nreturn count;");
        luaBuilder.append("\nend");
        //执行计算器自加
        luaBuilder.append("\ncount = redis.call('incr',KEYS[1])");
        //从第一次调用开始限流
        luaBuilder.append("\nif tonumber(count) == 1 then");
        //设置过期时间
        luaBuilder.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        luaBuilder.append("\nend");
        luaBuilder.append("\nreturn count;");
        return luaBuilder.toString();
    }
}
