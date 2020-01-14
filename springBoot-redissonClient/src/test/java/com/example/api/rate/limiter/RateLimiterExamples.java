package com.example.api.rate.limiter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RateLimiterExamples {
    /**
     * redisson client对象
     */
    @Autowired
    private RedissonClient redisson;

    @Test
    public void test() throws Exception {
//        RMap<Object, Object> myLimiter = redisson.getMap("myLimiter");
//        myLimiter.expire(10, TimeUnit.HOURS);
        RRateLimiter limiter = redisson.getRateLimiter("myLimiter");
        // one permit per 2 seconds
        limiter.trySetRate(RateType.OVERALL, 1, 2, RateIntervalUnit.SECONDS);

        CountDownLatch latch = new CountDownLatch(2);
        limiter.acquire(1);
        latch.countDown();

        Thread t = new Thread(() -> {
            limiter.acquire(1);

            latch.countDown();
        });
        t.start();
        t.join();

        latch.await();

        redisson.shutdown();

    }

}