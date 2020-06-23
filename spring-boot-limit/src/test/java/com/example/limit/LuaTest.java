package com.example.limit;

import java.util.Arrays;
import java.util.Collections;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;


public class LuaTest {

    public static void main(String[] args) {

        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 1000, "123456", 0);
        Jedis jedis = jedisPool.getResource();
        try {
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

            String lua = luaBuilder.toString();
            System.out.println(lua);

            Object obj = null;
            for (int i = 0; i <= 12; i++) {
                obj = jedis.evalsha(jedis.scriptLoad(lua), Arrays.asList("localhost"),
                        Arrays.asList("10", "6000"));
                System.out.println(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Long executeLua(JedisPool jedisPool, String luaScript, String key, Integer count, Integer rateLimit) {
        Object obj = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            obj = jedis.eval(jedis.scriptLoad(luaScript), Collections.singletonList(key),
                    Arrays.asList(String.valueOf(count), String.valueOf(rateLimit)));
        } catch (JedisException ex) {
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
        return (Long) obj;
    }

}