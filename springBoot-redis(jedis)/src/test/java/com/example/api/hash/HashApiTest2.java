package com.example.api.hash;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * redis => 数据结构 hash 获取 相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HashApiTest2 {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HashApiTest2.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取hash中属性值
     * hash结构 => hget 命令测试
     */
    @Test
    public void hasSetApiTest() {
        String key = "hset_api_test_key";

        String field = "hset_api_test_field2";

        String value = "hset_api_test_value1";

        int duration = 60;
        //初始化测试数据
        Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value, duration, TimeUnit.SECONDS);
        LOGGER.info("hset  end  =>  hasSetFlag={}", hasSetFlag);
    }


}