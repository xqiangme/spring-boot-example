package com.example.api.list;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * redis => 数据结构 list 相关命令 测试
 *
 * @author 码农猿
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ListApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListApiTest.class);

    @Autowired
    private RedisCache redisCache;

    private String LIST_KEY = "list_key";

    /**
     * 添加到列表头部
     * list结构 => lPush 命令测试
     */
    @Test
    public void lPushApiTest() {
        String value = "lpush_api_test_value";
        //添加到列表头部
        long listSize = redisCache.lPush(LIST_KEY, value);
        LOGGER.info("lpush tes end listSize={}", listSize);
    }

    /**
     * 添加到列表尾部
     * list结构 => rpush 命令测试
     */
    @Test
    public void rpushApiTest() {
        String value = "rpush_api_test_value";
        //添加到列表尾部
        long listSize = redisCache.rpush(LIST_KEY, value);
        LOGGER.info("rpush test end listSize={}", listSize);
    }

    /**
     * 移出并获取列表的第一个元素
     * list结构 => lpop  命令测试
     */
    @Test
    public void lpopApiTest() {
        //移出并获取列表的第一个元素
        String value = redisCache.lpop(LIST_KEY, String.class);
        LOGGER.info("lpop tes end value={}", value);
    }

    /**
     * 移出并获取列表的最后一个元素
     * list结构 => rpop  命令测试
     */
    @Test
    public void rpopApiTest() {
        //移出并获取列表的最后一个元素
        String value = redisCache.rpop(LIST_KEY, String.class);
        LOGGER.info("rpop tes end value={}", value);
    }

    /**
     * 根据值移出元素
     * list结构 => lrem  命令测试
     */
    @Test
    public void lremApiTest() {
        String key = "lrem_api_test_key";
        String value1 = "lrem_api_test_value_1";
        String value2 = "lrem_api_test_value_2";
        String value3 = "lrem_api_test_value_3";
        //初始化测试数据
        redisCache.lPush(key, value1);
        redisCache.lPush(key, value2);
        redisCache.lPush(key, value3);

        //移除指定value 值元素
        Long lremFlag = redisCache.lRem(key, value2);
        LOGGER.info("lrem test end value={}", lremFlag);
    }

    /**
     * 按照索引(下标)范围修剪列表
     * list结构 => ltrim  命令测试
     */
    @Test
    public void ltrimApiTest() {
        String key = "ltrim_api_test_key";

        //初始化10条测试数据
        for (int i = 1; i <= 10; i++) {
            String value1 = "lrem_api_test_value_".concat(String.valueOf(i));
            redisCache.lPush(key, value1);
        }
        //根据下标修剪list
        //注：从0 开始，
        Boolean ltrimFlag = redisCache.ltrim(key, 0, 5);
        LOGGER.info("lTrim test end ltrimFlag={}", ltrimFlag);
    }

    /**
     * 设置列表指定索引值为newValue
     *
     * list结构 => lset  命令测试
     */
    @Test
    public void lsetApiTest() {
        String key = "lset_api_test_key";

        //初始化10条测试数据
        for (int i = 1; i <= 10; i++) {
            String value1 = "lset_api_test_value_".concat(String.valueOf(i));
            redisCache.lPush(key, value1);
        }
        //修改元素
        Boolean lsetFlag = redisCache.lset(key, 5, "我是第六个元素");
        LOGGER.info("lset test end lsetFlag={}", lsetFlag);
    }


}