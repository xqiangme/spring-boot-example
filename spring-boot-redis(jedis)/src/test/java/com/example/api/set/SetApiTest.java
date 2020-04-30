package com.example.api.set;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * redis => 数据结构 set  相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SetApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SetApiTest.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 添加到集合
     * set 结构 => sadd 命令测试
     */
    @Test
    public void sAddApiTest() {
        String key = "sadd_api_test_key";
        String value = "sadd_api_test_value";
        //添加到集合
        Long saddSize = redisCache.setTemplate().sAdd(key, value);
        LOGGER.info("sadd  end  =>  saddSize={}", saddSize);
    }

    /**
     * 从集合删除元素
     * set 结构 => srem 命令测试
     */
    @Test
    public void sRemApiTest() {
        String key = "srem_api_test_key";
        String value = "srem_api_test_value";
        //初始化数据
        Long saddSize = redisCache.setTemplate().sAdd(key, value);
        LOGGER.info("sadd  end  =>  saddSize={}", saddSize);
        //添加到集合
        Long sremSize = redisCache.setTemplate().sRem(key, value);
        LOGGER.info("srem  end  =>  sremSize={}", sremSize);
    }

    /**
     * 获取集合大小
     * set 结构 => scard 命令测试
     */
    @Test
    public void sCardApiTest() {
        String key = "scard_api_test_key";
        String value1 = "scard_api_test_value_1";
        String value2 = "scard_api_test_value_2";
        //初始化数据
        Long saddSize1 = redisCache.setTemplate().sAdd(key, value1);
        Long saddSize2 = redisCache.setTemplate().sAdd(key, value2);
        LOGGER.info("sadd  end  =>  saddSize1={},saddSize2={}", saddSize1, saddSize2);
        //获取集合大小
        Long scardSize = redisCache.setTemplate().sCard(key);
        LOGGER.info("scard  end  =>  scardSize={}", scardSize);
    }

    /**
     * 判断value元素是否是集合key的成员
     * <p>
     * set 结构 => sismember 命令测试
     */
    @Test
    public void sismemberApiTest() {
        String key = "sismember_api_test_key";
        String value1 = "sismember_api_test_value_1";
        String value2 = "sismember_api_test_value_2";
        //初始化数据
        Long saddSize1 = redisCache.setTemplate().sAdd(key, value1);
        LOGGER.info("sadd  end  =>  saddSize1={}", saddSize1);
        //获取集合大小
        Boolean value1Exists = redisCache.setTemplate().sisMember(key, value1);
        Boolean value2Exists = redisCache.setTemplate().sisMember(key, value2);
        LOGGER.info("sismember  end  =>  value1Exists={},value2Exists={}", value1Exists, value2Exists);
    }

    /**
     * 返回集合中随机count个元素
     * <p>
     * set 结构 => srandmember 命令测试
     */
    @Test
    public void srandmemberApiTest() {
        String key = "srandmember_api_test_key";
        String value = "srandmember_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key, value);

        //获取集合中随机个数值
        Set<String> stringSet = redisCache.setTemplate().sRandMember(key, String.class, 5);
        if (CollectionUtils.isEmpty(stringSet)) {
            return;
        }
        for (String str : stringSet) {
            LOGGER.info("srandmember   =>  str={}", str);
        }

        LOGGER.info("srandmember test  end  stringSetSize={}", stringSet.size());
    }


    /**
     * 返回集合中所有元素
     * <p>
     * set 结构 => sismember 命令测试
     */
    @Test
    public void smembersApiTest() {
        String key = "smembers_api_test_key";
        String value = "smembers_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key, value);

        //获取集合所有元素
        Set<String> stringSet = redisCache.setTemplate().sMembers(key, String.class);
        if (CollectionUtils.isEmpty(stringSet)) {
            return;
        }
        for (String str : stringSet) {
            LOGGER.info("smembers   =>  str={}", str);
        }

        LOGGER.info("smembers test  end  stringSetSize={}", stringSet.size());
    }

    /**
     * 返回给定集合的交集
     * set 结构 => sismember 命令测试
     */
    @Test
    public void spopApiTest() {
        String key = "spop_api_test_key";
        String value = "spop_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key, value);

        //移除并返回集合中的一个随机元素
        String strValue = redisCache.setTemplate().sPop(key, String.class);

        LOGGER.info("spop test  end  strValue={}", strValue);
    }

    /**
     * 返回给定集合的交集
     * <p>
     * set 结构 => sinter 命令测试
     */
    @Test
    public void sinterApiTest() {
        String key1 = "sinter_api_test_key_1";
        String key2 = "sinter_api_test_key_2";
        String key3 = "sinter_api_test_key_3";
        String value = "sinter_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key1, value, 1, 5);
        this.buildBaseInfo(key2, value, 5, 10);
        this.buildBaseInfo(key3, value, 5, 5);

        Set<String> keys = new HashSet<>();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);
        //返回给定集合的交集
        Set<String> setValue = redisCache.setTemplate().sinter(keys, String.class);
        LOGGER.info("sinter test   => setValueSize={}", setValue.size());
        if (CollectionUtils.isEmpty(setValue)) {
            return;
        }
        for (String str : setValue) {
            LOGGER.info("sinter test   => str={}", str);
        }
        LOGGER.info("sinter test  end");
    }

    /**
     * 返回给定集合的并集
     * <p>
     * set 结构 => sunion 命令测试
     */
    @Test
    public void sunionApiTest() {
        String key1 = "sunion_api_test_key_1";
        String key2 = "sunion_api_test_key_2";
        String key3 = "sunion_api_test_key_3";
        String value = "sunion_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key1, value, 1, 5);
        this.buildBaseInfo(key2, value, 5, 10);
        this.buildBaseInfo(key3, value, 5, 15);

        Set<String> keys = new HashSet<>();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);
        //返回给定集合的并集
        Set<String> setValue = redisCache.setTemplate().sunion(keys, String.class);
        LOGGER.info("sunion test   => setValueSize={}", setValue.size());
        if (CollectionUtils.isEmpty(setValue)) {
            return;
        }
        for (String str : setValue) {
            LOGGER.info("sunion test   => str={}", str);
        }
        LOGGER.info("sunion test  end => setValueSize={}", setValue.size());
    }

    /**
     * 返回给定集合的差集
     * <p>
     * set 结构 => sdiff 命令测试
     */
    @Test
    public void sdiffApiTest() {
        String key1 = "sdiff_api_test_key_1";
        String key2 = "sdiff_api_test_key_2";
        String value = "sdiff_api_test_value_";
        //初始化数据
        this.buildBaseInfo(key1, value, 1, 5);
        this.buildBaseInfo(key2, value, 5, 10);

        List<String> keys = new ArrayList<>();
        keys.add(key2);
        keys.add(key1);

        //返回给定集合的差集
        Set<String> setValue = redisCache.setTemplate().sDiff(keys, String.class);
        LOGGER.info("sdiff test   => setValueSize={}", setValue.size());
        if (CollectionUtils.isEmpty(setValue)) {
            return;
        }
        for (String str : setValue) {
            LOGGER.info("sdiff test   => str={}", str);
        }
        LOGGER.info("sdiff test  end => setValueSize={}", setValue.size());
    }

    /**
     * 初始化数据
     */
    private void buildBaseInfo(String key, String defaultValue) {
        this.buildBaseInfo(key, defaultValue, 1, 20);
    }

    /**
     * 初始化数据
     */
    private void buildBaseInfo(String key, String defaultValue, int start, int end) {
        //先删除原先
        redisCache.keyTemplate().del(key);
        //初始化20条测试数据
        for (int i = start; i <= end; i++) {
            String value = defaultValue.concat(String.valueOf(i));
            Long saddSize = redisCache.setTemplate().sAdd(key, value);
            LOGGER.info("sadd  end  =>  i={}, saddSize={}", i, saddSize);
        }
    }
}