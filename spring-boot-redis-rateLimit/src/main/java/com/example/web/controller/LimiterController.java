package com.example.web.controller;

import com.example.ratelimit.annotation.RateLimit;
import com.example.web.param.OrderRateParam;
import com.example.web.param.UserPhoneCaptchaRateParam;
import com.example.web.param.UserRateParam;
import com.example.web.result.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述: 测试限流
 *
 * @author 程序员小强
 */
@RestController
public class LimiterController {

    /**
     * 计数器
     * 演示 demo 为了方便计数
     */
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final Map<Integer, AtomicInteger> COUNT_USER_MAP = new HashMap<>();
    private static final Map<String, AtomicInteger> COUNT_PHONE_MAP = new HashMap<>();
    private static final Map<String, AtomicInteger> COUNT_ORDER_MAP = new HashMap<>();

    /**
     * 普通限流
     * <p>
     * 30 秒中，可以访问10次
     */
    @RequestMapping("/limitTest")
    @RateLimit(key = "limit-test-key", time = 30, count = 10)
    public Response limitTest() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER.incrementAndGet());
        return Response.success(dataMap);
    }

    /**
     * 根据手机号限流-限制验证码发送次数
     * <p>
     * 示例：5分钟内，验证码最多发送10次
     */
    @RequestMapping("/limitByPhone")
    @RateLimit(key = "limit-phone-key", time = 300, count = 10, keyField = "phone", msg = "5分钟内，验证码最多发送10次")
    public Response limitByPhone(UserPhoneCaptchaRateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        if (COUNT_PHONE_MAP.containsKey(param.getPhone())) {
            COUNT_PHONE_MAP.get(param.getPhone()).incrementAndGet();
        } else {
            COUNT_PHONE_MAP.put(param.getPhone(), new AtomicInteger(1));
        }
        dataMap.put("times", COUNT_PHONE_MAP.get(param.getPhone()).intValue());
        dataMap.put("reqParam", param);
        return Response.success(dataMap);
    }

    /**
     * 根据用户ID限流示例
     * <p>
     * 300 秒中，可以访问10次
     */
    @RequestMapping("/limitUserId")
    @RateLimit(key = "limit-user-key", time = 300, count = 10, keyField = "userId", msg = "访问过快，请稍后再试!")
    public Response limitByUserId(UserRateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        if (COUNT_USER_MAP.containsKey(param.getUserId())) {
            COUNT_USER_MAP.get(param.getUserId()).incrementAndGet();
        } else {
            COUNT_USER_MAP.put(param.getUserId(), new AtomicInteger(1));
        }
        dataMap.put("times", COUNT_USER_MAP.get(param.getUserId()).intValue());
        dataMap.put("reqParam", param);
        return Response.success(dataMap);
    }

    /**
     * 根据订单ID限流示例
     * <p>
     * 300 秒中，可以访问10次
     */
    @RequestMapping("/limitByOrderId")
    @RateLimit(key = "limit-order-key", time = 300, count = 10, keyField = "orderId", msg = "订单飞走了，请稍后再试!")
    public Response limitByOrderId(OrderRateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        if (COUNT_ORDER_MAP.containsKey(param.getOrderId())) {
            COUNT_ORDER_MAP.get(param.getOrderId()).incrementAndGet();
        } else {
            COUNT_ORDER_MAP.put(param.getOrderId(), new AtomicInteger(1));
        }
        dataMap.put("times", COUNT_ORDER_MAP.get(param.getOrderId()).intValue());
        dataMap.put("reqParam", param);
        return Response.success(dataMap);
    }
}
