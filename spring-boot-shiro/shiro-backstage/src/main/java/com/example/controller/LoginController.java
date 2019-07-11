package com.example.controller;

import com.example.model.param.UserLoginParam;
import com.example.model.result.Response;
import com.example.model.result.UserLoginResult;
import com.example.service.UserInfoService;
import com.example.shiro.ShiroOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户登录
     */
    @PostMapping("/in")
    public Response<UserLoginResult> loginIn(@RequestBody UserLoginParam loginParam) {
        log.info("[登录] >> 用户登录开始");
        return Response.success(userInfoService.login(loginParam));
    }


    /**
     * 用户登出
     */
    @PostMapping("out")
    public Response loginOut() {
        log.info("[登录] >> 用户登出");
        ShiroOperation.loginOut();
        return Response.success();
    }

    /**
     * 获取登录用户信息
     */
    @PostMapping("/getUserInfo")
    public Response getUserInfo() {
        log.info("[登录] >> 获取登录用户信息");
        Map<String, Object> map = new HashMap<>();
        //当前登录用户
        map.put("currentUser", ShiroOperation.getCurrentUser());
        //权限信息
        map.put("permissions", ShiroOperation.getPermissions());
        return Response.success(map);
    }

}
