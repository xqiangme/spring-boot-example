package com.example.api.zset;

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
 * redis => 数据结构 sorted set  相关命令 测试
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZSetApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZSetApiTest.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 根据分值排序 添加到有序集合
     * sorted set结构 => zadd 命令测试
     */
    @Test
    public void zAddApiTest() {
        String key = "zAdd_api_test_key1";
        String value1 = "zAdd_api_test_value1";
        String value2 = "zAdd_api_test_value2";
        double score1 = 1.0;
        double score2 = 0.9;
        //添加有序集合成员
        Long addSize1 = redisCache.zSetTemplate().zAdd(key, value1, score1);
        Long addSize2 = redisCache.zSetTemplate().zAdd(key, value2, score2);
        LOGGER.info("zAdd end  =>  addSize1={},addSize2={}", addSize1, addSize2);
    }

    /**
     * 有序集合增分值
     * <p>
     * sorted set结构 => zincrby 命令测试
     */
    @Test
    public void zIncrbyApiTest() {
        String key = "zIncrby_api_test_key1";
        String value1 = "zIncrby_api_test_value1";
        double score1 = 1.0;
        //计数器分值
        double incrbyScore = redisCache.zSetTemplate().zIncrBy(key, value1, score1);
        LOGGER.info("zIncrby end  =>  incrbyScore={}", incrbyScore);
    }


    /**
     * 移除有序集合中成员
     * sorted set结构 => zrem 命令测试
     */
    @Test
    public void zremApiTest() {
        String key = "zRem_api_test_key1";
        String value1 = "zRem_api_test_value1";
        String value2 = "zRem_api_test_value2";
        double score1 = 1.0;
        double score2 = 0.9;
        //添加有序集合成员
        Long addSize1 = redisCache.zSetTemplate().zAdd(key, value1, score1);
        Long addSize2 = redisCache.zSetTemplate().zAdd(key, value2, score2);
        LOGGER.info("zAdd end  =>  addSize1={},addSize2={}", addSize1, addSize2);
        Long zremSize = redisCache.zSetTemplate().zRem(key, value2);
        LOGGER.info("zRem end  =>  zremSize={}", zremSize);
    }

    /**
     * 移除指定排名内的成员(从小到大)
     * sorted set结构 => zremrangebyrank  命令测试
     */
    @Test
    public void zremrangebyrankApiTest() {
        String key = "zremrangebyrank_api_test_key1";
        String value = "zremrangebyrank_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, value);
        //移除指定排名内的成员(从小到大) 移除排名  2-6元素
        Long size = redisCache.zSetTemplate().zRemRangeByRank(key, 1, 5);
        LOGGER.info("zRemRangeByRank end  =>  size={}", size);
    }

    /**
     * 移除指定分数区间内的成员(从小到大)
     * sorted set结构 => zremrangebyscore   命令测试
     */
    @Test
    public void zRemRangeByScoreApiTest() {
        String key = "zremrangebyscore_api_test_key1";
        String value = "zremrangebyscore_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, value);
        //移除指定分数区间内的成员(从小到大) 分值排名  3-6元素
        Long size = redisCache.zSetTemplate().zRemRangeByScore(key, 2, 5);
        LOGGER.info("zRemRangeByScore end  =>  size={}", size);
    }


    /**
     * 获取有序集合大小
     * sorted set结构 => zCard   命令测试
     */
    @Test
    public void zCardApiTest() {
        String key = "zcard_api_test_key";
        String value = "zcard_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, value);

        //获取有序集合大小
        Long size = redisCache.zSetTemplate().zCard(key);
        LOGGER.info("zCard end  =>  size={}", size);
    }


    /**
     * 获取有序集合,成员的分值
     * <p>
     * sorted set结构 => zscore 命令测试
     */
    @Test
    public void zScoreApiTest() {
        String key = "zscore_api_test_key";
        String value = "zscore_api_test_value";
        double score = 0.9;
        //添加有序集合成员
        Long addSize = redisCache.zSetTemplate().zAdd(key, value, score);
        LOGGER.info("zAdd end  =>  addSize={}", addSize);
        Double doubleValue = redisCache.zSetTemplate().zScore(key, value);
        LOGGER.info("zScore end  =>  doubleValue={}", doubleValue);
    }


    /**
     * 获取指定分数范围内的成员数量
     * sorted set结构 => zCard   命令测试
     */
    @Test
    public void zCountApiTest() {
        String key = "zCount_api_test_key";
        String value = "zCount_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, value);

        // 获取指定分数范围内的成员数量
        Long size = redisCache.zSetTemplate().zCount(key, 2, 10);
        LOGGER.info("zCount end  =>  size={}", size);
    }

    /**
     * 获取成员索引排名(按分值从小到大,从0开始)
     * <p>
     * sorted set结构 => zrank 命令测试
     */
    @Test
    public void zrankApiTest() {
        String key = "zrank_api_test_key";
        String defaultValue = "zrank_api_test_value_";
        String value = "zrank_api_test_value_1";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取成员索引排名(按分值从小到大,从0开始)
        Long zRankscore = redisCache.zSetTemplate().zRank(key, value);
        LOGGER.info("zrank end  =>  zRankscore={}", zRankscore);
    }

    /**
     * 获取成员索引排名(按分值从大到小)
     * <p>
     * sorted set结构 => zrevrank 命令测试
     */
    @Test
    public void zrevrankApiTest() {
        String key = "zrevrank_api_test_key";
        String defaultValue = "zrevrank_api_test_value_";
        String value = "zrevrank_api_test_value_20";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取成员索引排名(按分值从大到小)
        Long score = redisCache.zSetTemplate().zRevRank(key, value);
        LOGGER.info("zrevrank end  =>  score={}", score);
    }

    /**
     * 获取指定索引范围内有序集合升序成员(分值从小到大)
     * <p>
     * sorted set结构 => zrange 命令测试
     */
    @Test
    public void zRangeApiTest() {
        String key = "zrange_api_test_key";
        String defaultValue = "zrange_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取指定索引范围内有序集合升序成员(分值从小到大)
        List<String> stringList = redisCache.zSetTemplate().zRange(key, String.class, 1, 10);
        LOGGER.info("zRange end  =>  size={}", stringList.size());
        if (CollectionUtils.isEmpty(stringList)) {
            return;
        }
        for (String str : stringList) {
            LOGGER.info("zRange => str={}", str);
        }
        LOGGER.info("zRange test end");
    }

    /**
     * 获取指定索引范围内有序集合升序成员(分值从大到小)
     * <p>
     * sorted set结构 => zrevrange  命令测试
     */
    @Test
    public void zRevRangeApiTest() {
        String key = "zrevrange_api_test_key";
        String defaultValue = "zrevrange_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取指定索引范围内有序集合升序成员(分值从大到小)
        List<String> stringList = redisCache.zSetTemplate().zRevRange(key, String.class, 1, 10);
        LOGGER.info("zrevrange end  =>  size={}", stringList.size());
        if (CollectionUtils.isEmpty(stringList)) {
            return;
        }
        for (String str : stringList) {
            LOGGER.info("zrevrange => str={}", str);
        }
        LOGGER.info("zrevrange test end");
    }


    /**
     * 获取指定分值范围内有序集合升序成员(分值从小到大)
     * <p>
     * sorted set结构 => zrange 命令测试
     */
    @Test
    public void zRangeByScoreApiTest() {
        String key = "zrangebyscore_api_test_key";
        String defaultValue = "zrangebyscore_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取指定分值范围内有序集合升序成员(分值从小到大)
        List<String> stringList = redisCache.zSetTemplate().zRangeByScore(key, String.class, 1, 10);
        LOGGER.info("zrangebyscore end  =>  size={}", stringList.size());
        if (CollectionUtils.isEmpty(stringList)) {
            return;
        }
        for (String str : stringList) {
            LOGGER.info("zrangebyscore => str={}", str);
        }
        LOGGER.info("zrangebyscore test end");
    }

    /**
     * 获取指定分值范围内有序集合升序成员(分值从大到小)
     * <p>
     * sorted set结构 => zrange 命令测试
     */
    @Test
    public void zRevRangeByScoreApiTest() {
        String key = "zrevrangebyscore_api_test_key";
        String defaultValue = "zrevrangebyscore_api_test_value_";
        //初始化测试数据
        this.buildBaseInfo(key, defaultValue);
        //获取指定分值范围内有序集合升序成员(分值从大到小)
        List<String> stringList = redisCache.zSetTemplate().zRevRangeByScore(key, String.class, 1, 10);
        LOGGER.info("zrevrangebyscore end  =>  size={}", stringList.size());
        if (CollectionUtils.isEmpty(stringList)) {
            return;
        }
        for (String str : stringList) {
            LOGGER.info("zrevrangebyscore => str={}", str);
        }
        LOGGER.info("zrevrangebyscore test end");
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
            Long saddSize = redisCache.zSetTemplate().zAdd(key, value, i);
            LOGGER.info("zAdd  end  =>  i={}, zaddSize={}", i, saddSize);
        }
    }
}