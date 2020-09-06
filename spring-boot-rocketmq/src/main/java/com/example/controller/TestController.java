package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.example.config.property.MqProperty;
import com.example.model.OrderInfoModel;
import com.example.model.UserInfoModel;
import com.example.util.MqSendHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 程序员小强
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private MqProperty mqConfig;

    @Autowired
    @Qualifier("msgProducer")
    private ProducerBean msgProducer;

    private AtomicInteger num = new AtomicInteger(10000);

    /**
     * 普通消息
     */
    @RequestMapping("/normalMsg")
    public Object normalMsg() {

        int id = num.getAndIncrement();
        UserInfoModel user = new UserInfoModel();
        user.setUserId(String.valueOf(id));
        user.setUsername("zhangsan" + id);
        user.setRealName("张三" + id);
        user.setAge(id * 2);
        user.setRemarks("普通消息");

        log.info("普通消息发送 > user:{}", JSON.toJSONString(user));
        return MqSendHelperUtil.send(msgProducer, mqConfig.getTopic(), mqConfig.getTag(), id, user);
    }


    /**
     * 延时消息
     */
    @RequestMapping("/delayMsg")
    public Object delayMsg() {
        //延时分钟数
        int minutes = 10;
        Date date = new Date(TimeUnit.MINUTES.toMillis(minutes));
        int id = num.getAndIncrement();
        UserInfoModel user = new UserInfoModel();
        user.setUserId(String.valueOf(id));
        user.setUsername("lisi" + id);
        user.setRealName("李四" + id);
        user.setAge(id * 2);
        user.setRemarks(MessageFormat.format("延时消息 > {0}", this.formatDate(date)));

        log.info("延时消息发送 > user:{}", JSON.toJSONString(user));
        return MqSendHelperUtil.sendDelayMinutes(msgProducer, mqConfig.getTopic(), mqConfig.getTag(), id, user, 10);
    }


    /**
     * 订单普通消息
     */
    @RequestMapping("/orderNormalMsg")
    public Object orderNormalMsg() {
        int id = num.getAndIncrement();
        OrderInfoModel user = new OrderInfoModel();
        user.setOrderId(String.valueOf(id));
        user.setTitle("zhangsan" + id);
        user.setRemarks("订单消息测试");

        log.info("订单普通消息发送 > user:{}", JSON.toJSONString(user));
        return MqSendHelperUtil.send(msgProducer, mqConfig.getOrderTopic(), mqConfig.getOrderTag(), id, user);
    }


    /**
     * 时间格式化
     */
    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}