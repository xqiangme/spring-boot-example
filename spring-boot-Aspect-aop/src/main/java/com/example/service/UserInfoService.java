package com.example.service;

import com.example.bean.param.UserAddParam;
import com.example.bean.result.Response;

/**
 * 人员管理接口
 *
 * @author 码农猿
 */
public interface UserInfoService {

    /**
     * 添加人员接口
     *
     * @author 码农猿
     */
    Response addUser(UserAddParam addParam);
}