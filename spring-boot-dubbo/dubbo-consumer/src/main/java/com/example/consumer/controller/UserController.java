package com.example.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.model.UserInfoVO;
import com.example.api.service.UserApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者服务 测试
 *
 * @author 码农猿
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 引用服务
     */
    @Reference(version = "${dubbo.provider.version}", application = "${dubbo.application.id}")
    private UserApiService userApiService;

    /**
     * 添加人员
     */
    @RequestMapping("/save")
    public Object saveUser(@RequestBody UserInfoVO user) {
        LOGGER.info("【消费者服务】>> 添加人员");
        return userApiService.saveUser(user);
    }

    /**
     * 获取人员列表
     */
    @RequestMapping("/list")
    public Object listUser() {
        LOGGER.info("【消费者服务】>> 获取人员列表");
        return userApiService.listUser();
    }

    /**
     * 测试统一异常处理
     */
    @RequestMapping("/exception")
    public Object testException(@RequestParam("type") Integer type) {
        LOGGER.info("【消费者服务】>> 测试异常处理");
        return userApiService.testException(type);
    }
}
