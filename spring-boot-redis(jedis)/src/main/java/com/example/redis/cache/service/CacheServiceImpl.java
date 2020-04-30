/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.redis.cache.service;

import com.example.redis.cache.annotation.CacheData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-24 21:25
 * @desc
 */
@Service
public class CacheServiceImpl implements CacheService {
    private static final Log log = LogFactory.getLog(CacheServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Object> getTest1(String type) {
        log.info("CacheServiceImpl >> getTest1 start type:" + type);
        Map<String, List<Object>> map = new HashMap<>();
        map.put("1", Arrays.asList("1001,1002,1003,1004".split(",")));
        map.put("2", Arrays.asList("2001,2002,2003,2004".split(",")));
        map.put("3", Arrays.asList("3001,3002,3003,3004".split(",")));
        if (map.containsKey(type)) {
            return map.get(type);
        }
        log.info("CacheServiceImpl >> getTest1 start end:" + type);

        return Arrays.asList("default,".split(","));
    }


    @CacheData(expireTime = 11 * 60, storageNullFlag = true)
    @Override
    public List<Object> getTest2(String type) {
        log.info("CacheServiceImpl >> getTest2 start type:" + type);
//        Jedis jedis = jedisPool.getResource();
//
//        byte[] key = "TestKey001".getBytes();
//        byte[] valueRedis = jedis.get(key);
//        if (null != valueRedis) {
//            Map<String, List<Object>> deserialize = (Map<String, List<Object>>) SerializableUtil.deserialize(valueRedis);
//            log.info("CacheServiceImpl >> getTest2 start form redis end:" + type);
//
//            return deserialize.get(type);
//
//        }


        Map<String, List<Object>> map = new HashMap<>();
        map.put("1", Arrays.asList("1001,1002,1003,1004".split(",")));
        map.put("2", Arrays.asList("2001,2002,2003,2004".split(",")));
        map.put("3", Arrays.asList("3001,3002,3003,3004".split(",")));

//        jedis.set(key, SerializableUtil.serialize(map));
        log.info("CacheServiceImpl >> getTest2 start end:" + type);

        return map.get(type);
    }
}
