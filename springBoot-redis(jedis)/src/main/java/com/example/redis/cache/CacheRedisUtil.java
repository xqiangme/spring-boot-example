package com.example.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CacheRedisUtil {

    public static Boolean exists(JedisPool jedisPool, byte[] key) {
        if (null == key || null == jedisPool) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception ex) {
            return false;
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }

    public static byte[] get(JedisPool jedisPool, byte[] key) {
        if (null == key || null == jedisPool) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception ex) {
            return null;
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }

    public static String set(JedisPool jedisPool, byte[] key, int seconds, byte[] value) {
        if (null == key || null == jedisPool) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.setex(key, seconds, value);
        } catch (Exception ex) {
            return null;
        } finally {
            if (jedis != null) {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }
        }
    }
}
