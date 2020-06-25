package com.example.ratelimit.component;

import com.example.ratelimit.exception.RateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;
import java.util.Collections;

/**
 * Redis限流组件
 *
 * @author 程序员小强
 */
@Component
public class RedisRateLimitComponent {
    private static final Logger logger = LoggerFactory.getLogger(RedisRateLimitComponent.class);

    private JedisPool jedisPool;

    @Autowired
    public RedisRateLimitComponent(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 限流方法
     * 1.执行 lua 表达式
     * 2.通过 lua 表达式实现-限流计数器
     *
     * @param redisKey
     * @param time           超时时间-秒数
     * @param rateLimitCount 限流次数
     */
    public Long rateLimit(String redisKey, Integer time, Integer rateLimitCount) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object obj = jedis.evalsha(jedis.scriptLoad(this.buildLuaScript()), Collections.singletonList(redisKey),
                    Arrays.asList(String.valueOf(rateLimitCount), String.valueOf(time)));
            return Long.valueOf(obj.toString());
        } catch (JedisException ex) {
            logger.error("[ executeLua ] >> messages:{}", ex.getMessage(), ex);
            throw new RateLimitException("[ RedisRateLimitComponent ] >> jedis run lua script exception" + ex.getMessage());
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }

    /**
     * 构建lua 表达式
     * KEYS[1] -- 参数key
     * ARGV[1]-- 失效时间段内最大放行次数
     * ARGV[2]-- 失效时间|秒
     */
    private String buildLuaScript() {
        StringBuilder luaBuilder = new StringBuilder();
        //定义变量
        luaBuilder.append("local count");
        //获取调用脚本时传入的第一个key值（用作限流的 key）
        luaBuilder.append("\ncount = redis.call('get',KEYS[1])");
        // 获取调用脚本时传入的第一个参数值（限流大小）-- 调用不超过最大值，则直接返回
        luaBuilder.append("\nif count and tonumber(count) > tonumber(ARGV[1]) then");
        luaBuilder.append("\nreturn count;");
        luaBuilder.append("\nend");
        //执行计算器自增
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