package com.example.web.controller;

import com.example.annotation.RateLimit;
import com.example.web.result.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class LimiterController {

    /**
     * 计数器
     */
    private static final AtomicInteger COUNTER = new AtomicInteger();

    /**
     * 30 秒中，可以访问10次
     */
    @RateLimit(key = "limit-test01-key", time = 30, count = 10)
    @RequestMapping("/test")
    public Response limitTest01() {
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", date);
        dataMap.put("times", COUNTER.incrementAndGet());
        return Response.success(dataMap);
    }
}
