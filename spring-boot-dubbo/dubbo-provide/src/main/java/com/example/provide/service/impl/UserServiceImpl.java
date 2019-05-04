package com.example.provide.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.api.model.UserInfoVO;
import com.example.api.service.UserApiService;
import com.example.provide.constant.MockDataConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 人员接口-实现
 *
 * @author 码农猿
 */
@Service(version = "${dubbo.provider.version}", application = "${dubbo.application.id}")
public class UserServiceImpl implements UserApiService {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 添加人员
     */
    @Override
    public UserInfoVO saveUser(UserInfoVO user) {
        long timeMillis = System.currentTimeMillis();
        LOGGER.info("【生成者服务】>> 添加人员接口 >> 执行开始 , 请求时间戳={} user={}", timeMillis, user);
        user.setId(MockDataConstant.userMockList.size() + 1);
        MockDataConstant.userMockList.add(user);
        LOGGER.info("【生成者服务】>> 添加人员接口 >> 执行开始 , 请求时间戳={} user={}", timeMillis, user);
        return user;
    }

    /**
     * 获取人员列表
     */
    @Override
    public List<UserInfoVO> listUser() {
        LOGGER.info("【生成者服务】>> 获取人员列表 >> 接 , 请求时间戳={}",  System.currentTimeMillis());
        return MockDataConstant.userMockList;
    }
}