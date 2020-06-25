package com.example.service;

import com.example.web.param.UserRateParam;
import com.example.web.param.UserUpdateParam;

/**
 * 限流测试接口
 *
 * @author 程序员小强
 */
public interface UserRateLimitService {

    /**
     * 无参数测试
     *
     */
    void noArgs();

    /**
     * 单个-参数限流测试
     *
     * @param userId
     */
    void updateUserOneParam(Integer userId);

    /**
     * 多个-参数限流测试
     *
     * @param userId
     * @param userName
     */
    void updateUserTwoParam(Integer userId, String userName);

    /**
     * 单个对象参数限流测试
     *
     * @param param
     */
    void updateUserOneObjParam(UserUpdateParam param);

    /**
     * 多个个对象参数限流测试
     *
     * @param param1
     * @param param2
     */
    void updateUserTwoObjParam(UserUpdateParam param1, UserRateParam param2);

    /**
     * 基本类型+对象参数
     *
     * @param param
     * @param userId
     */
    void updateUserManyParam(UserUpdateParam param, Integer userId);

}