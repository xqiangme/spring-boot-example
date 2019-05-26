package com.example.service.impl;

import com.example.annotation.ApiAnnotation;
import com.example.bean.param.UserAddParam;
import com.example.bean.result.Response;
import com.example.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 人员管理接口
 *
 * @author 码农猿
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {


    /**
     * 添加人员接口
     *
     * @author 码农猿
     */
    @Override
    @ApiAnnotation(description = "添加人员")
    public Response addUser(UserAddParam addParam) {

        log.info("人员管理 UserInfoServiceImpl >> 添加人员接口开始");

        if (1 == 1) {
           throw  new RuntimeException("模拟异常了。。。。");
        }
        log.info("人员管理 UserInfoServiceImpl >> 添加人员接口结束");
        return Response.success();
    }
}