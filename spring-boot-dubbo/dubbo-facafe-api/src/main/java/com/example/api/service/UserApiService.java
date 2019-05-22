package com.example.api.service;

import com.example.api.model.UserInfoVO;
import com.example.api.result.ApiResult;

import java.util.List;

/**
 * 公共对外接口 - 人员管理
 *
 * @author 码农猿
 */
public interface UserApiService {

    /**
     * 添加人员
     */
    UserInfoVO saveUser(UserInfoVO user);


    /**
     * 获取人员列表
     */
    List<UserInfoVO> listUser();


    /**
     * 测试生产者异常
     */
    ApiResult testException(Integer type);


}
