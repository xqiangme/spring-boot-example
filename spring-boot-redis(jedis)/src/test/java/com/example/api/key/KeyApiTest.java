package com.example.api.key;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * redis => key 相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class KeyApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyApiTest.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 检查key是否存在
     */
    @Test
    public void existsApiTest() {
        String key = "exists_api_test_key";
        String value = "exists_api_test_value";
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);

        //检查key是否存在
        Boolean existFlag = redisCache.keyTemplate().exists(key);

        LOGGER.info("检查key是否存在 existFlag={}", existFlag);
    }

    /**
     * 为key设置过期时间，以秒计算
     */
    @Test
    public void expireApiTest() {
        String key = "expire_api_test_key";
        String value = "expire_api_test_value";
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);
        int seconds = 120;
        //设置key 过期时间
        Boolean expireFlag = redisCache.keyTemplate().expire(key, seconds);

        LOGGER.info(" 为key设置过期时间，以秒计算 expireFlag={}", expireFlag);
    }

    /**
     * 删除key
     */
    @Test
    public void delApiTest() {
        String key = "del_api_test_key";
        String value = "del_api_test_value";
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);
        //删除key
        Long del = redisCache.keyTemplate().del(key);
        LOGGER.info("删除key数量 del={}", del);
    }

    /**
     * 获取key所存储的数据类型
     */
    @Test
    public void typeApiTest() {
        String key = "type_api_test_key";
        String value = "type_api_test_value";
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);
        String type = redisCache.keyTemplate().getType(key);
        LOGGER.info("获取key所存储的数据类型 type={}", type);
    }
}