package com.example.web.controller;

import com.example.service.UserRateLimitService;
import com.example.web.param.UserRateParam;
import com.example.web.param.UserUpdateParam;
import com.example.web.result.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述: 测试页
 *
 * @author 程序员小强
 */
@RestController
public class UserLimiterController {

    /**
     * 计数器
     * 演示 demo 为了方便计数
     */
    private static final AtomicInteger COUNTER1 = new AtomicInteger();
    private static final AtomicInteger COUNTER2 = new AtomicInteger();
    private static final AtomicInteger COUNTER3 = new AtomicInteger();
    private static final AtomicInteger COUNTER4 = new AtomicInteger();
    private static final AtomicInteger COUNTER5 = new AtomicInteger();
    private static final AtomicInteger COUNTER6 = new AtomicInteger();

    @Autowired
    private UserRateLimitService userRateLimitService;

    /**
     * 无参数
     */
    @RequestMapping("/limitNoArgs")
    public Response noArgs() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER1.incrementAndGet());
        userRateLimitService.noArgs();
        return Response.success(dataMap);
    }

    /**
     * 单个参数限流测试
     */
    @RequestMapping("/limitUpdateUserOneParam")
    public Response updateUserOneParam(@RequestParam("userId") Integer userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER2.incrementAndGet());
        userRateLimitService.updateUserOneParam(userId);
        return Response.success(dataMap);
    }

    /**
     * 多个参数限流测试
     */
    @RequestMapping("/limitUpdateUserTwoParam")
    public Response updateUserTwoParam(@RequestParam("userId") Integer userId, @RequestParam("userName") String userName) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER3.incrementAndGet());
        userRateLimitService.updateUserTwoParam(userId, userName);
        return Response.success(dataMap);
    }

    /**
     * 单个对象参数限流测试
     *
     * @param param
     */
    @RequestMapping("/limitUpdateUserOneObjParam")
    public Response updateUserOneObjParam(UserUpdateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER4.incrementAndGet());
        userRateLimitService.updateUserOneObjParam(param);
        return Response.success(dataMap);
    }

    /**
     * 对象参数限流测试
     */
    @RequestMapping("/limitUpdateUserTwoObjParam")
    public Response updateUserTwoObjParam(UserUpdateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER5.incrementAndGet());
        UserRateParam param2 = new UserRateParam();
        param2.setUserId(1000001);
        userRateLimitService.updateUserTwoObjParam(param, param2);
        return Response.success(dataMap);
    }

    /**
     * 限流测试
     */
    @RequestMapping("/limitUpdateUserManyParam")
    public Response updateUserManyParam(UserUpdateParam param) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        dataMap.put("times", COUNTER6.incrementAndGet());

        userRateLimitService.updateUserManyParam(param, 6000001);
        return Response.success(dataMap);
    }

}
