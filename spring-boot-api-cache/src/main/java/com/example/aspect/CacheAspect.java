package com.example.aspect;

import com.alibaba.fastjson.JSON;
import com.example.cache.annotation.CacheData;
import com.example.redisson.RedisCache;
import com.example.util.CacheContext;
import com.example.util.SerializableUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 统一缓存自定义注解拦截实现
 *
 * @author mengq
 */
@Aspect
@Component
public class CacheAspect {
    private static final Logger log = LoggerFactory.getLogger(CacheAspect.class);
    private static final String EMPTY = "";
    private static final String POINT = ".";
    private static final String CACHE_KEY_PREFIX = "cache.aspect:";
    private static final String LOCK_KEY_PREFIX = "lock.";

    @Resource
    private RedisCache redisCache;

    /**
     * redisson client对象
     */
    @Resource
    private RedissonClient redisson;

    /**
     * 匹配所有使用以下注解的方法
     * 注意：缓存是基于类+方法+参数内容做的缓存key,重载方法可能会出现问题
     * 禁止在同一个类，方法名相同的两个方法使用
     *
     * @see CacheData
     */
    @Pointcut("@annotation(com.example.cache.annotation.CacheData)")
    public void pointCut() {
    }

    /**
     * 拦截添加缓存注解的方法
     *
     * @param pjpParam
     * @return
     * @throws Throwable
     * @see CacheData
     */
    @Around("pointCut()&&@annotation(cacheData)")
    public Object logAround(ProceedingJoinPoint pjpParam, CacheData cacheData) throws Throwable {
        //注解为空
        if (null == cacheData) {
            return pjpParam.proceed(pjpParam.getArgs());
        }
        //仅用于方法
        if (null == pjpParam.getSignature() || !(pjpParam.getSignature() instanceof MethodSignature)) {
            return pjpParam.proceed(pjpParam.getArgs());
        }
        //方法实例
        Method method = ((MethodSignature) pjpParam.getSignature()).getMethod();
        //方法类名
        String className = pjpParam.getSignature().getDeclaringTypeName();
        //方法名
        String methodName = method.getName();

        log.debug("[ CacheAspect ] >> notReadCacheFlag:{} , refreshCacheFlag :{} , method:{} ",
                CacheContext.notReadCacheFlag.get(), CacheContext.refreshCacheFlag.get(), className + methodName);
        //已设置-不需要读取缓存
        if (null != CacheContext.notReadCacheFlag.get() && CacheContext.notReadCacheFlag.get()) {
            return pjpParam.proceed(pjpParam.getArgs());
        }

        //生成缓存key
        String cacheKey = this.getCacheKey(cacheData, className, methodName, pjpParam);
        //已设置-需要刷新缓存
        if (null != CacheContext.refreshCacheFlag.get() && CacheContext.refreshCacheFlag.get()) {
            Object resultValue = pjpParam.proceed(pjpParam.getArgs());
            //刷新缓存
            redisCache.putByte(cacheKey, SerializableUtil.serialize(resultValue, cacheKey), this.getExpireTime(cacheData));
            return resultValue;
        }

        Object value = null;
        //缓存中读取值
        byte[] cacheByteValue = redisCache.getByte(cacheKey);
        if (null != cacheByteValue && cacheByteValue.length > 0) {
            //反序列化对象
            value = SerializableUtil.deserialize(cacheByteValue);
        }
        //缓存中存在并且不为空-直接返回
        if (null != value && !cacheKey.equals(value.toString())) {
            log.info("[ CacheAspect ] >> [ get from cache in first ] method:{} , cacheKey:{}", className + methodName, cacheKey);
            return value;
        }
        //如果设置了允许存储null值，若缓存key存在，并且value与自定义key相同 > 则直接返回 null
        if (cacheData.storageNullFlag() && redisCache.exists(cacheKey)) {
            log.info("[ CacheAspect ] >> [ get from cache in first value is null ] method:{} , cacheKey:{}", className + methodName, cacheKey);
            return null;
        }

        //若缓存中不存在 > 则执行方法，并重入缓存
        //加锁防止大量穿透
        RLock lock = redisson.getLock(LOCK_KEY_PREFIX + cacheKey);
        lock.lock();
        try {
            //二次尝试从缓存中读取
            byte[] cacheByteValueSecond = redisCache.getByte(cacheKey);
            if (null != cacheByteValueSecond && cacheByteValueSecond.length > 0) {
                //反序列化对象
                value = SerializableUtil.deserialize(cacheByteValueSecond);
            }
            //缓存中存在并且不为空-直接返回
            if (null != value && !cacheKey.equals(value.toString())) {
                log.info("[ CacheAspect ] >> [ get from cache in second ] method:{} , cacheKey:{}", className + methodName, cacheKey);
                return value;
            }
            //如果设置了允许存储null值，若缓存key存在，并且value与自定义key相同 > 则直接返回 null
            if (cacheData.storageNullFlag() && redisCache.exists(cacheKey)) {
                log.info("[ CacheAspect ] >> [ get from cache in second value is null ] method:{} , cacheKey:{}", className + methodName, cacheKey);
                return null;
            }

            //执行方法-并获得返回值
            value = pjpParam.proceed(pjpParam.getArgs());

            //返回值不为空-存入缓存并返回
            if (null != value) {
                //存入缓存
                redisCache.putByte(cacheKey, SerializableUtil.serialize(value, cacheKey), this.getExpireTime(cacheData));
                return value;
            }

            //返回值为空-是否设置需要存储空对象
            if (cacheData.storageNullFlag()) {
                //存入缓存,空返回值时value也存储key
                redisCache.putByte(cacheKey, SerializableUtil.serialize(cacheKey), this.getExpireTime(cacheData));
                return null;
            }
            return null;
        } finally {
            //解锁
            lock.unlock();
            log.info("[ CacheAspect ] >> un lock method:{} , cacheKey:{}", className + methodName, cacheKey);
        }
    }

    /**
     * 生成缓存key
     *
     * @param cacheData
     * @param className
     * @param methodName
     * @param pjpParam
     * @return
     */
    private String getCacheKey(CacheData cacheData, String className, String methodName, ProceedingJoinPoint pjpParam) {
        //缓存key前缀
        String keyPrefix = cacheData.keyPrefix();
        if (EMPTY.equals(keyPrefix)) {
            keyPrefix = methodName;
        }
        //方法全路径（类名+方法名）
        String methodPath = className + POINT + methodName;
        //若方法参数为空
        if (pjpParam.getArgs() == null || pjpParam.getArgs().length == 0) {
            return CACHE_KEY_PREFIX + keyPrefix + POINT + DigestUtils.md5Hex(methodPath);
        }
        //参数序号
        int i = 0;
        //按照参数顺序,拼接方法参数
        Map<String, Object> paramMap = new LinkedHashMap<>(pjpParam.getArgs().length);
        for (Object obj : pjpParam.getArgs()) {
            i++;
            if (obj != null) {
                paramMap.put(obj.getClass().getName() + i, obj);
            } else {
                paramMap.put("NULL" + i, "NULL");
            }
        }
        String paramJson = JSON.toJSONString(paramMap);
        log.debug("[ CacheAspect ] >> param-JSON:{}", JSON.toJSONString(paramMap));
        return CACHE_KEY_PREFIX + keyPrefix + POINT + DigestUtils.md5Hex(paramJson);
    }

    /**
     * 计算过期时间 如果缓存设置了需要延迟失效，
     * 取设置的延迟时间1-2倍之间的一个随机值作为真正的延迟时间值
     */
    private int getExpireTime(CacheData cacheData) {
        int expire = cacheData.expireTime();

        //设置为0，则默认效期到当天的截止
        if (expire == 0) {
            expire = (int) (60 * 60 * 24 - ((System.currentTimeMillis() / 1000 + 8 * 3600) % (60 * 60 * 24)));
        }

        int randomExpire = 0;
        //若设置>0 , 失效时间加上（0~设置）的值内的随机数
        if (cacheData.randomExpire() > 0) {
            randomExpire = new Random().nextInt(cacheData.randomExpire());
        }

        return expire + randomExpire;
    }
}
