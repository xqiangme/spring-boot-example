package com.example.redis;

import com.example.bloomfilter.RedisBitMapBloomFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;

/**
 * @author 程序员小强
 */
public class RedisBloomFilterTest {

    /**
     * 预估元素数量
     */
    private static final int MAX_ELEMENT_SIZE = 10000 * 120;

    /**
     * 误差率
     */
    private static final double FPP = 0.001;

    /**
     * 过期时间-秒
     */
    private static final int EXPIRE_SECOND = 60 * 60 * 24;

    private static final String REDIS_KEY = "my_test_bloom_filter";

    private static RedisBitMapBloomFilter redisBloomFilter;

    @BeforeClass
    public static void beforeClass() throws Exception {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 1000, "123456", 0);
        redisBloomFilter = new RedisBitMapBloomFilter(REDIS_KEY, EXPIRE_SECOND, MAX_ELEMENT_SIZE, FPP, jedisPool);
        System.out.println("Hash 函数个数 : " + redisBloomFilter.getHashFunctionNum());
        System.out.println("bit数组长度 : " + redisBloomFilter.getBitmapLength());
    }

    @Test
    public void testPut() {
        redisBloomFilter.put(1001);
        redisBloomFilter.put(1002);
        redisBloomFilter.put(1003);
        redisBloomFilter.put(1004);
        redisBloomFilter.put(1005);
    }

    @Test
    public void testContains() throws Exception {
        System.out.println(redisBloomFilter.contains(1001));
        System.out.println(redisBloomFilter.contains(1002));
        System.out.println(redisBloomFilter.contains(1003));
        System.out.println(redisBloomFilter.contains(1006));
    }

    @Test
    public void testPutMany() {
        long time1 = System.currentTimeMillis();
        //插入数据 0 ~ 100w
        for (int i = 0; i < 1000000; i++) {
            redisBloomFilter.put(String.valueOf(i));
        }
        long time2 = System.currentTimeMillis();
        System.out.println("输入录入结束 ：" + (time2 - time1));
        int count = 0;
        //测试误判
        for (int i = 1000000; i < 2000000; i++) {
            if (redisBloomFilter.contains(String.valueOf(i))) {
                //累加误判次数
                count++;
            }
        }
        System.out.println("是否存在耗时 ：" + (System.currentTimeMillis() - time2));
        System.out.println("总共的误判数:" + count);
        System.out.println("误判率：" + new BigDecimal(count).divide(BigDecimal.valueOf(1000000)));
        System.out.println("执行结束 ：" + (System.currentTimeMillis() - time1));
    }

}