package com.example.controller;

import com.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/demo1")
    public String demo1() {
        return "success";
    }

    @RequestMapping("/demo2")
    public Object demo2() {
        return userInfoService.listAll();
    }

}
