package com.example.api.list;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * redis => 数据结构 list 查询 相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ListQueryApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListQueryApiTest.class);

    @Autowired
    private RedisCache redisCache;

    private String LIST_KEY = "list_key";


    /**
     * 设置列表指定索引值为newValue
     * <p>
     * list结构 => lrang,len,lindex  命令测试
     */
    @Test
    public void listApiTest() {
        String key = "lrange_api_test_key";
        String value = "lrange_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, value);

        //修改元素  2-11个元素
        List<String> stringList = redisCache.listTemplate().lRange(key, 1, 10);
        if (!CollectionUtils.isEmpty(stringList)) {
            for (String str : stringList) {
                LOGGER.info("lrange =>   str={}", str);
            }
        }
        //当前集合大小
        long len = redisCache.listTemplate().lLen(key);
        LOGGER.info("len test end len={}", len);
        if (len > 0) {
            String svalue = redisCache.listTemplate().lIndex(key, 0, String.class);
            LOGGER.info("lindex test end svalue={}", svalue);
        }
        LOGGER.info("list test end");
    }


    /**
     * 初始化数据
     */
    private void buildBaseInfo(String key, String defaultValue) {
        //先删除原先
        redisCache.keyTemplate().del(key);
        //初始化20条测试数据
        for (int i = 1; i <= 20; i++) {
            String value = defaultValue.concat(String.valueOf(i));
            redisCache.listTemplate().rpush(key, value);
        }
    }
}