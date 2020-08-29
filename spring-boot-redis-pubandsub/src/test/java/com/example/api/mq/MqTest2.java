package com.example.api.mq;

import com.example.pubsub.RedisPubSubClient;
import com.example.pubsub.enums.MessesChannel;
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
 * @author 程序员小强
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MqTest2 {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(MqTest2.class);

    @Autowired
    private RedisPubSubClient redisPubSubClient;

    @Test
    public void publishUserMsgTest() throws Exception {
        log.info(" [ 消息发布开始 ] ...");
        for (int i = 1; i <= 10; i++) {
            redisPubSubClient.publish(MessesChannel.USER_REGISTER.name(), "用户注册消息。。。" + i);
        }
        log.info(" [ 用户新增消息发布完毕 ] ...");
        Thread.sleep(3000 * 2);
//        for (int i = 1; i <= 10; i++) {
//            redisPubSubClient.publish(MessesChannel.ORDER_ADD.name(), "订单创建消息。。。" + i);
//        }
//        log.info(" [ 订单新增消息发布完毕 ] ...");

        Thread.sleep(3000);
    }
}