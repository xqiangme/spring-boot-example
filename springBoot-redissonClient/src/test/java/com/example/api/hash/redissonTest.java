package com.example.api.hash;

import com.example.redisson.RedisCache;
import com.example.redisson.RedissonLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class redissonTest {

    @Autowired
    private RedissonLock redissonLock;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void test() throws Exception {
        String redisKey = "zAdd_api_test_key1";

        for (int i = 0; i <= 10; i++) {
            String order = String.valueOf(i);
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("线程: " + threadName + " 当前 i " + order);
                try {
                    boolean isLocked = redissonLock.tryLock(redisKey, 3L, 10L, TimeUnit.SECONDS);
                    if (isLocked) {
                        System.out.println("线程: " + threadName + " 加锁成功了 .....");
                    }
                } finally {
                    redissonLock.unlock(redisKey);
                    System.out.println("线程: " + threadName + " 解锁完毕了 .....");
                }
            }).start();
        }
        Thread.sleep(3000);
    }

}