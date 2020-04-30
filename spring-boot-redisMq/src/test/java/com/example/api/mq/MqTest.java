package com.example.api.mq;

import com.example.config.RedisMQClient;
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
 * @author mengqiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MqTest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MqTest.class);

    @Autowired
    private RedisMQClient redisMQClient;

    /**
     * 添加到列表头部
     * list结构 => lPush 命令测试
     */
    @Test
    public void lPushApiTest() {
        System.out.println(null == redisMQClient);
        for (int i = 0; i < 100; i++) {
            redisMQClient.publish("PID", "1_messge" + i);
        }
    }


}