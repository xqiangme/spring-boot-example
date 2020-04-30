package com.example.api.lock;

import com.example.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * redis => 分布式锁 相关命令
 *
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LockApiTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LockApiTest.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 检查key是否存在
     */
    @Test
    public void test() {

        String lockKey = "lock_api_test_key2";
        String requestId = "lock_api_test_request_id";

        //超时毫秒数
        int expireMillisecond = 10000;
        try {
            boolean lock = redisCache.lock().tryGetLock(lockKey, requestId, expireMillisecond);
            System.out.println("加锁 lock=" + lock);

            if (!lock) {
                System.out.println("加锁失败了！！！");
                return;
            }

            //模拟-业务处理
            Thread.sleep(2000);


        } catch (Exception e) {
            System.out.println("执行异常了 ");
        } finally {
            boolean releaseLockFlag = redisCache.lock().releaseLock(lockKey, requestId);
            System.out.println("解锁 releaseLockFlag = " + releaseLockFlag);
        }
    }

}