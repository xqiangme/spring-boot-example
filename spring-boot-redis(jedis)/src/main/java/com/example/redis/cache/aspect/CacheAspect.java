package com.example.redis.cache.aspect;

import com.example.redis.cache.CacheContext;
import com.example.redis.cache.CacheRedisUtil;
import com.example.redis.cache.SerializableUtil;
import com.example.redis.cache.annotation.CacheData;
import com.example.redis.cache.annotation.IgnoreCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 统一缓存自定义注解拦截实现
 *
 * @author mengq
 */
@Aspect
@Component
public class CacheAspect {

    private static final Log log = LogFactory.getLog(CacheAspect.class);
    private static final String EMPTY = "";
    private static final String POINT = ".";

    @Resource
    private JedisPool jedisPool;
    private Lock lock = new ReentrantLock();

    /**
     * 拦截添加缓存注解的方法
     *
     * @param pjpParam
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.example.redis.cache.annotation.CacheData)")
    public Object doCache(ProceedingJoinPoint pjpParam) throws Throwable {
        Signature sig = pjpParam.getSignature();
        if (!(sig instanceof MethodSignature)) {
            return pjpParam.proceed(pjpParam.getArgs());
        }
        //方法实例
        Method method = ((MethodSignature) pjpParam.getSignature()).getMethod();
        //获取到方法上的注解
        CacheData cacheData = method.getAnnotation(CacheData.class);
        //注解为空
        if (null == cacheData) {
            return pjpParam.proceed(pjpParam.getArgs());
        }
        //方法类名
        String className = pjpParam.getTarget().getClass().getCanonicalName();
        //方法名
        String methodName = method.getName();
        //缓存key
        byte[] cacheKey = this.getCacheKey(cacheData, className, methodName, pjpParam).getBytes();

        //已设置不读取缓存
        if (null != CacheContext.refreshFlag.get() && CacheContext.refreshFlag.get()) {
            return pjpParam.proceed(pjpParam.getArgs());
        }

        Object value = null;
        //缓存中读取值
        byte[] cacheByteValue = CacheRedisUtil.get(jedisPool, cacheKey);
        if (null != cacheByteValue && cacheByteValue.length > 0) {
            //反序列化对象
            value = SerializableUtil.deserialize(cacheByteValue);
        }

        //缓存中存在并且不为空-直接返回
        if (!ObjectUtils.isEmpty(value) && !new String(cacheKey).equals(value)) {
            log.info("[ CacheAspect ] >> cacheKey:" + new String(cacheKey) + ",class:" + className + ",method:" + methodName + " first get data from cache ");
            return value;
        }
        //如果设置了允许存储null值，若缓存key存在，并且value与自定义key相同 > 则直接返回 null
        if (cacheData.storageNullFlag() && CacheRedisUtil.exists(jedisPool, cacheKey)) {
            log.info("[ CacheAspect ] >> cacheKey:" + new String(cacheKey) + ",class:" + className + ",method:" + methodName + " first get data from cache  is null ");
            return null;
        }

        //若缓存中不存在 > 则执行方法，并重入缓存
        //加锁防止大量穿透
        lock.lock();
        try {
            //二次尝试从缓存中读取
            byte[] cacheByteValueSecond = CacheRedisUtil.get(jedisPool, cacheKey);
            if (null != cacheByteValueSecond && cacheByteValueSecond.length > 0) {
                //反序列化对象
                value = SerializableUtil.deserialize(cacheByteValueSecond);
            }
            //缓存中存在并且不为空-直接返回
            if (!ObjectUtils.isEmpty(value) && !new String(cacheKey).equals(value.toString())) {
                log.info("[ CacheAspect ] >> cacheKey:" + new String(cacheKey) + ",class:" + className + ",method:" + methodName + " second get data from cache ");
                return value;
            }
            //如果设置了允许存储null值，若缓存key存在，并且value与自定义key相同 > 则直接返回 null
            if (cacheData.storageNullFlag() && CacheRedisUtil.exists(jedisPool, cacheKey)) {
                log.info("[ CacheAspect ] >> cacheKey:" + new String(cacheKey) + ",class:" + className + ",method:" + methodName + " second get data from cache  is null ");
                return null;
            }

            //执行方法-并获得返回值
            value = pjpParam.proceed(pjpParam.getArgs());

            //返回值不为空-存入缓存并返回
            if (!ObjectUtils.isEmpty(value)) {
                //存入缓存
                CacheRedisUtil.set(jedisPool, cacheKey, this.getExpireTime(cacheData), SerializableUtil.serialize(value));
                return value;
            }

            //返回值为空-是否设置需要存储空对象
            if (cacheData.storageNullFlag()) {
                //存入缓存,value也存储key
                CacheRedisUtil.set(jedisPool, cacheKey, this.getExpireTime(cacheData), SerializableUtil.serialize(new String(cacheKey)));
                return value;
            }
            return value;
        } finally {
            //解锁
            lock.unlock();
            log.info("[ CacheAspect ] >> cacheKey:" + new String(cacheKey) + ",class:" + className + ",method:" + methodName + " close jedis end ");
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
            return keyPrefix + POINT + DigestUtils.md5Hex(methodPath);
        }

        Annotation[][] annotations = ((MethodSignature) pjpParam.getSignature()).getMethod().getParameterAnnotations();
        List<Integer> ignoreList = new ArrayList<>();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].length <= 0) {
                continue;
            }
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j].annotationType().equals(IgnoreCache.class)) {
                    ignoreList.add(i);
                }
            }
        }
        int i = 0;
        StringBuilder paramKey = new StringBuilder();
        for (Object obj : pjpParam.getArgs()) {
            if (ignoreList.contains(i++)) {
                continue;
            }
            if (obj != null) {
                paramKey.append(obj.toString());
            } else {
                paramKey.append("NULL");
            }
        }
        return keyPrefix + POINT + DigestUtils.md5Hex(methodPath + paramKey);
    }

    /**
     * 计算过期时间 如果缓存设置了需要延迟失效，
     * 取设置的延迟时间1-2倍之间的一个随机值作为真正的延迟时间值
     */
    private int getExpireTime(CacheData cacheData) {
        int expire = cacheData.expireTime();

        if (expire == 0) {
            expire = (int) (60 * 60 * 24 - ((System.currentTimeMillis() / 1000 + 8 * 3600) % (60 * 60 * 24)));
        }
        int offset = 0;
        return expire + offset;
    }

}
