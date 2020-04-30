package com.example.api.hash;

import com.example.redisson.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HashTest2 {


    @Autowired
    private RedisCache redisCache;

    /**
     *
     */
    @Test
    public void test1() throws Exception {
        String key = "set.12345566-001";
        RSet<Object> set01 = redisCache.getRedisSet(key);
        System.out.println("first-set01 = " + (null == set01) + " size=" + set01.size() + " exist=" + set01.isExists());
        set01.add("001");
//        set01.expire(1, TimeUnit.HOURS);
        System.out.println("second-set01 = " + (null == set01) + " size=" + set01.size() + " exist=" + set01.isExists());

        RSet<Object> set02 = redisCache.getRedisSet("set.12345566-002");
        System.out.println("set02 = " + (null == set02) + " size=" + set02.size() + " exist=" + set02.isExists());

        RSet<Object> set03 = redisCache.getRedisSet("set.12345566-003");
        System.out.println("set03 = " + (null == set03) + " size=" + set03.size() + " exist=" + set03.isExists());

    }

}