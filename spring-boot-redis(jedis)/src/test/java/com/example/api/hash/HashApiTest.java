package com.example.api.hash;

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
import java.util.Map;
import java.util.Set;

/**
 * redis => 数据结构 hash 获取 相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HashApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HashApiTest.class);

    @Autowired
    private RedisCache redisCache;
    
    /**
     * 获取hash中属性值
     * hash结构 => hget 命令测试
     */
    @Test
    public void hasSetApiTest() {
        String key = "hset_api_test_key1";
        String value = "hset_api_test_value1";
        String field = "hset_api_test_field2";
        //初始化测试数据
        Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
        LOGGER.info("hset  end  =>  hasSetFlag={}", hasSetFlag);
    }

    /**
     * 获取hash中属性值
     * hash结构 => hget 命令测试
     */
    @Test
    public void hasGetApiTest() {
        String key = "hget_api_test_key";
        String value = "hget_api_test_value";
        String field = "hget_api_test_field";
        //初始化测试数据
        Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
        LOGGER.info("hset  end  =>  hasSetFlag={}", hasSetFlag);
        String hashValue = redisCache.hashTemplate().hGet(key, field, String.class);
        LOGGER.info("hget test end  =>  hashValue={}", hashValue);
    }

    /**
     * 删除属性值
     * hash结构 => hdel 命令测试
     */
    @Test
    public void hasDelApiTest() {
        String key = "hdel_api_test_key";
        String value = "hdel_api_test_value";
        String field = "hdel_api_test_field";
        //初始化测试数据
        Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
        LOGGER.info("hset  end  =>  hasSetFlag={}", hasSetFlag);
        //返回删除的数量
        Long delNum = redisCache.hashTemplate().hdel(key, field);
        LOGGER.info("hdel test end  =>  hashValue={}", delNum);
    }

    /**
     * 判断属性是否存在
     * hash结构 => hexists  命令测试
     */
    @Test
    public void hExistsApiTest() {
        String key = "hexists_api_test_key";
        String value = "hexists_api_test_value";
        String field = "hexists_api_test_field";
        //初始化测试数据
        Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
        LOGGER.info("hset  end  =>  hasSetFlag={}", hasSetFlag);
        //返回是否存在
        Boolean hExistsFlag = redisCache.hashTemplate().hexists(key, field);
        LOGGER.info("hexists test end  =>  hExistsFlag={}", hExistsFlag);
    }

    /**
     * 获取hash长度
     * hash结构 => hlen  命令测试
     */
    @Test
    public void hLenApiTest() {
        String key = "hlen_api_test_key";
        for (int i = 1; i <= 10; i++) {
            String str = String.valueOf(i);
            String field = "hlen_api_test_field_".concat(str);
            String value = "hlen_api_test_value_".concat(str);
            //初始化10条测试数据
            Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
            LOGGER.info("hlen  end  => i={} hasSetFlag={}", i, hasSetFlag);
        }
        //获取hash 长度
        Long hashLen = redisCache.hashTemplate().hlen(key);
        LOGGER.info("hlen test end  =>  hashLen={}", hashLen);
    }

    /**
     * hash 计数器
     * hash结构 => hincrby    命令测试
     */
    @Test
    public void hIncrByApiTest() {
        String key = "hincrby_api_test_key";
        String field = "hincrby_api_test_field";
        long value = 1;
        //测试10次计数
        for (int i = 1; i <= 10; i++) {
            Long incrValue = redisCache.hashTemplate().hashIncrBy(key, field, value);
            LOGGER.info("hashIncr  end  => i={} incrValue={}", i, incrValue);
        }
        Integer incrNum = redisCache.hashTemplate().hGet(key, field, Integer.class);
        LOGGER.info("hincrby test end  =>  incrNum={}", incrNum);
    }

    /**
     * hash 计数器
     * hash结构 => hincrbyfloat    命令测试
     */
    @Test
    public void hashIncrByFloatApiTest() {
        String key = "hincrbyfloat_api_test_key";
        String field = "hincrbyfloat_api_test_field";
        double value = 0.01;
        //测试10次计数
        for (int i = 1; i <= 10; i++) {
            Double incrValue = redisCache.hashTemplate().hashIncrByFloat(key, field, value);
            LOGGER.info("hincrbyfloat  end  => i={} incrValue={}", i, incrValue);
        }
        Double incrNum = redisCache.hashTemplate().hGet(key, field, Double.class);
        LOGGER.info("hincrbyfloat test end  =>  incrNum={}", incrNum);
    }

    /**
     * 获取所有属性与值 map
     * hash结构 => hgetall    命令测试
     */
    @Test
    public void hgetAllApiTest() {
        String key = "hgetall_api_test_key2";
        for (int i = 1; i <= 10; i++) {
            String str = String.valueOf(i);
            String field = "hgetall_api_test_field_".concat(str);
            String value = "hgetall_api_test_value_".concat(str);
            //初始化10条测试数据
            Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
            LOGGER.info("hset  end  => i={} hasSetFlag={}", i, hasSetFlag);
        }

        Map<String, String> hashAllMap = redisCache.hashTemplate().hgetAll(key, String.class);
        LOGGER.info("hgetAll test   =>  size={}", hashAllMap.size());
        if (!CollectionUtils.isEmpty(hashAllMap)) {
            for (Map.Entry<String, String> map : hashAllMap.entrySet()) {
                LOGGER.info("hgetAll test =>  field={},value={}", map.getKey(), map.getValue());
            }
        }
        LOGGER.info("hgetAll test end ");
    }

    /**
     * 获取所有属性的值内容
     * hash结构 => hvals    命令测试
     */
    @Test
    public void hvalsApiTest() {
        String key = "hvals_api_test_key";
        for (int i = 1; i <= 10; i++) {
            String str = String.valueOf(i);
            String field = "hvals_api_test_field_".concat(str);
            String value = "hvals_api_test_value_".concat(str);
            //初始化10条测试数据
            Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
            LOGGER.info("hset  end  => i={} hasSetFlag={}", i, hasSetFlag);
        }

        List<String> stringList = redisCache.hashTemplate().hvals(key, String.class);
        LOGGER.info("hvals test   =>  size={}", stringList.size());
        if (!CollectionUtils.isEmpty(stringList)) {
            for (String str : stringList) {
                LOGGER.info("hvals test =>  str={}", str);
            }
        }
        LOGGER.info("hvals test end ");
    }

    /**
     * 获取所有属性key
     * hash结构 => hkeys    命令测试
     */
    @Test
    public void hkeysApiTest() {
        String key = "hkeys_api_test_key";
        for (int i = 1; i <= 10; i++) {
            String str = String.valueOf(i);
            String field = "hkeys_api_test_field_".concat(str);
            String value = "hkeys_api_test_value_".concat(str);
            //初始化10条测试数据
            Boolean hasSetFlag = redisCache.hashTemplate().hSet(key, field, value);
            LOGGER.info("hset  end  => i={} hasSetFlag={}", i, hasSetFlag);
        }

        Set<String> stringSet = redisCache.hashTemplate().hkeys(key);
        LOGGER.info("hkeys test   =>  size={}", stringSet.size());
        if (!CollectionUtils.isEmpty(stringSet)) {
            for (String str : stringSet) {
                LOGGER.info("hkeys test =>  str={}", str);
            }
        }
        LOGGER.info("hkeys test end ");
    }

    /**
     * 若不存在则设置hash key 对应field的value
     * <p>
     * hash结构 => hsetNx 命令测试
     */
    @Test
    public void hasSetNxApiTest() {
        String key = "hsetNx_api_test_key1";
        String value = "hsetNx_api_test_value1";
        String field = "hsetNx_api_test_field2";
        //初始化测试数据
        Boolean hasSetNxFlag = redisCache.hashTemplate().hSetNx(key, field, value);
        LOGGER.info("hsetNx  end  =>  hasSetFlag={}", hasSetNxFlag);
    }
}