package com.example.api.string;

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
import java.util.List;

/**
 * redis => 数据结构 String 相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StringApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringApiTest.class);

    @Autowired
    private RedisCache redisCache;


    /**
     * 设置值
     */
    @Test
    public void setApiTest() {
        String key = "set_api_test_key";
        String value = "set_api_test_value";
        System.out.println(redisCache == null);
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);
        LOGGER.info("set test end");
    }


    /**
     * 获取key对应的value
     */
    @Test
    public void getApiTest() {

        String key = "get_api_test_key";
        String value = "get_api_test_value";
        //初始化测试数据
        redisCache.stringTemplate().set(key, value);
        //获取key 的值
        String stringValue = redisCache.stringTemplate().get(key);
        LOGGER.info("get test =>  value={}", stringValue);
    }


    /**
     * 累加计数器
     */
    @Test
    public void incrApiTest() {
        String key = "incr_api_test_key";
        //计数器
        Long incrValue = redisCache.stringTemplate().incr(key);
        LOGGER.info("incr test end  =>  incrValue={}", incrValue);
    }

    /**
     * 累减计数器
     */
    @Test
    public void decrApiTest() {
        String key = "decr_api_test_key";
        //自减计数器
        Long decrValue = redisCache.stringTemplate().decr(key);
        LOGGER.info("decr test end  =>  decrValue={}", decrValue);
    }

    /**
     * 自定义增量=> 累加计数器
     */
    @Test
    public void incrByApiTest() {
        String key = "incrBy_api_test_key";
        //自定义增量
        long num = 10;
        //计数器
        Long incrValue = redisCache.stringTemplate().incrBy(key, num);
        LOGGER.info("incrBy test end  =>  incrByValue={}", incrValue);
    }

    /**
     * 自定义减量=> 累减计数器
     */
    @Test
    public void decrByApiTest() {
        String key = "decrBy_api_test_key";
        //自定义减量
        long num = 10;
        //计数器
        Long decrValue = redisCache.stringTemplate().decrBy(key, num);
        LOGGER.info("decrBy test end  =>  decrValue={}", decrValue);
    }

    /**
     * key不存在,才设置  => 常用与分布式锁
     */
    @Test
    public void setNxApiTest() {
        String key = "setNx_api_test_key";
        long value = 1;
        //key不存在,才设置
        Boolean setNxflag = redisCache.stringTemplate().setNx(key, value);
        LOGGER.info("setNx test end  =>  setNxflag={}", setNxflag);
    }

    /**
     * 设置key-value,并返回旧值
     */
    @Test
    public void getSetApiTest() {
        String key = "getSet_api_test_key";
        //自定义增量
        String newValue = "new Value 01";
        //设置key-value,并返回旧值
        String oldValue = redisCache.stringTemplate().getSet(key, newValue);
        LOGGER.info("getSet test end  =>  oldValue={}", oldValue);
    }


    /**
     * 批量获取 api  测试
     */
    @Test
    public void mGetApiTest() {
        String defaultKey = "mSetAndGet_";
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            String stri = String.valueOf(i);
            String key = defaultKey.concat(stri);
            String vale = "value_".concat(stri);
            stringList.add(key);
            redisCache.stringTemplate().set(key, vale);
        }

        List<String> mgetList = redisCache.stringTemplate().mget(stringList, String.class);
        LOGGER.info("mget test end  =>  mgetListSize={}", mgetList.size());
        if (CollectionUtils.isEmpty(mgetList)) {
            return;
        }
        for (String str : mgetList) {
            LOGGER.info("mget test item  =>  str={}", str);
        }

    }
}