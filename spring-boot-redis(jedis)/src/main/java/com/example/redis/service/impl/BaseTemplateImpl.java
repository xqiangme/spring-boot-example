package com.example.redis.service.impl;

import com.example.redis.client.RedisClient;
import com.example.util.TimeUnitUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * 基础公共方法
 *
 * @author mengqiang
 */
public class BaseTemplateImpl {

    @Autowired
    protected JedisPool jedisPool;
    @Autowired
    protected RedisClient redisClient;

    /**
     * 默认失效时间毫秒 5 分钟
     */
    public static final long DEFAULT_CACHE_MILLIS = TimeUnitUtil.getMillis(TimeUnit.MINUTES, 5);

    protected static final int ZERO = 0;
    protected static final int FIVE = 5;
    protected static final int THIRTY = 30;
    protected static final String OK = "OK";
    protected static final Long LONG_ONE = 1L;
    protected static final Long LONG_ZERO = 0L;

    /**
     * 校验参数
     */
    protected void validateParam(String key, Object value) {
        this.validateKeyParam(key);
        Assert.notNull(value, "value不能为空");
        Assert.isInstanceOf(Object.class, value, "value没有实现Object接口，无法序列化");
    }

    /**
     * 校验参数
     */
    protected void validateKeyParam(String key) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(jedisPool, "jedis连接初始化失败");
    }

    /**
     * 判断long值是否相同
     */
    protected Boolean isLongEquals(Long valueOne, Long valueTwo) {
        if ((null != valueOne) && (null != valueTwo)) {
            return valueOne.intValue() == valueTwo.intValue();
        }
        return false;
    }

    /**
     * 判断string值是否相同
     */
    protected Boolean isStringEquals(String strOne, String strTwo) {
        if ((StringUtils.isNoneBlank(strOne)) && (StringUtils.isNoneBlank(strTwo))) {
            return strOne.equals(strTwo);
        }
        return false;
    }
}