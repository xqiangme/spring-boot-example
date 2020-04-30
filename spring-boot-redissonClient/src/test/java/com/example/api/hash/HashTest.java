package com.example.api.hash;

import com.example.redisson.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HashTest {


    @Autowired
    private RedisCache redisCache;

    /**
     *
     */
    @Test
    public void test1() throws Exception {
        String key = "hash.12345566";
        RMapCache<String, String> hashMap = redisCache.getRedisMap(key);
        hashMap.expire(1, TimeUnit.HOURS);
        hashMap.put("001", "001-value");
        //hashMap.put("002", "002-value", 50, TimeUnit.SECONDS);
        System.out.println("hashMap size=" + hashMap.size());

        System.out.println(hashMap.get("001"));
        System.out.println(hashMap.get("002"));
//        for (Map.Entry<String, String> map : hashMap.entrySet()) {
//            System.out.println("key: " + map.getKey() + "  value:" + map.getValue());
//        }

    }
}