package com.example.controller;

import com.example.bean.param.UserAddParam;
import com.example.bean.result.Response;
import com.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 人员管理接口
 *
 * @author 码农猿
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 添加人员
     *
     * @author 码农猿
     */
    @PostMapping(value = "/add-user")
    public Response addUser(@RequestBody UserAddParam addParam) {
        return userInfoService.addUser(addParam);
    }

}