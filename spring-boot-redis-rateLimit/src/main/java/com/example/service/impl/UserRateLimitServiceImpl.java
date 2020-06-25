/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.ratelimit.annotation.RateLimit;
import com.example.service.UserRateLimitService;
import com.example.web.param.UserRateParam;
import com.example.web.param.UserUpdateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 限流测试接口
 *
 * @author 程序员小强
 */
@Service
public class UserRateLimitServiceImpl implements UserRateLimitService {
    private static final Logger logger = LoggerFactory.getLogger(UserRateLimitServiceImpl.class);


    /**
     * 无参数测试
     */
    @Override
    @RateLimit(key = "limit-no-args-param", time = 300, count = 10)
    public void noArgs() {
        logger.info("[ noArgs ] >> noArgs ");
    }

    /**
     * 单个-参数限流测试
     * 非自定义参数类，使用 keyField = "userId",不生效，因为或取不到参数名
     *
     * @param userId
     */
    @Override
    @RateLimit(key = "limit-update-user-one-param", time = 300, count = 10, keyField = "userId", msg = "updateUserOneParam over max times")
    public void updateUserOneParam(Integer userId) {
        logger.info("[ updateUserOneParam ] >> userId={}", userId);

    }

    @Override
    @RateLimit(key = "limit-update-user-two-param", time = 300, count = 6, keyField = "userId", msg = "updateUserTwoParam over max times")
    public void updateUserTwoParam(Integer userId, String userName) {
        logger.info("[ updateUserTwoParam ] >> userId={}， userName={}", userId, userName);

    }

    @Override
    @RateLimit(key = "limit-update-user-one-obj-param", time = 300, count = 6, keyField = "userId", msg = "updateUserOneObjParam over max times")
    public void updateUserOneObjParam(UserUpdateParam param) {
        logger.info("[ updateUserOneObjParam ] >> {}", JSON.toJSONString(param));
    }

    @Override
    @RateLimit(key = "limit-update-user-two-obj-param", time = 300, count = 6, keyField = "userId", msg = "updateUserTwoObjParam over max times")
    public void updateUserTwoObjParam(UserUpdateParam param1, UserRateParam param2) {
        logger.info("[ updateUserTwoObjParam ] >> param1:{} param2:{}", JSON.toJSONString(param1), JSON.toJSONString(param2));

    }


    @Override
    @RateLimit(key = "limit-update-user-many-param", time = 300, count = 6, keyField = "userId", msg = "updateUserManyParam over max times")
    public void updateUserManyParam(UserUpdateParam param, Integer userId) {
        logger.info("[ updateUserManyParam ] >> {}-- userId:{}", JSON.toJSONString(param), userId);
    }
}