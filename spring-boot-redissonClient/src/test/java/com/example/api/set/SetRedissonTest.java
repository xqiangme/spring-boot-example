package com.example.api.set;

import com.example.redisson.RedisCache;
import org.checkerframework.checker.units.qual.K;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.MapOptions;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SetRedissonTest {

    @Autowired
    private RedisCache redisCache;



    @Test
    public void test() throws Exception {
        String redisKey = "set_api_test_key1_big";

        long start = System.currentTimeMillis();
        RSet<Object> set1 = redisCache.getRedisSet(redisKey);
        System.out.println("当前set1大小" + set1.size());
//        //存储5000
//        for (int i = 1; i <= 5000; i++) {
//            set1.add("test" + i);
//        }
        long end = System.currentTimeMillis();
        System.out.println("存储耗时" + (end - start));

        //获取当前Set
        RSet<Object> set2 = redisCache.getRedisSet(redisKey);
        long end2 = System.currentTimeMillis();
        System.out.println("当前set2大小" + set2.size());
        System.out.println("二次获取耗时" + (end2 - end));

        set2.forEach(a -> {
//            System.out.println(a);
        });
    }
}