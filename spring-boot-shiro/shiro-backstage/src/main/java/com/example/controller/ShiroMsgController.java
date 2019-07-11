package com.example.controller;

import com.example.shiro.ShiroOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前用户各种变量获取和操作
 */
@Slf4j
@RestController
@RequestMapping("/shiro-msg")
public class ShiroMsgController {


    /**
     * 用户信息
     */
    @PostMapping("/current-user")
    public Map<String, Object> getCurrentUser() {
        Map<String, Object> map = new HashMap<>();
        //当前登录用户
        map.put("currentUser", ShiroOperation.getCurrentUser());
        //权限信息
        map.put("permissions", ShiroOperation.getPermissions());
        return map;
    }


}
