package com.example.controller;

import com.example.config.ApplicationParam;
import com.example.config.UserConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {

    private ApplicationParam applicationParam;
    private UserConfigParam userConfigParam;

    @Autowired
    public TestController(ApplicationParam applicationParam, UserConfigParam userConfigParam) {
        this.applicationParam = applicationParam;
        this.userConfigParam = userConfigParam;
    }

    @RequestMapping(value = "/")
    public String HelloWorld() {
        return "HelloWorld stat success";
    }

    @RequestMapping("/hello")
    public String HelloWorldAll() {
        return "Hello world AppId= " + applicationParam.getAppId() + ", AppName=" + applicationParam.getAppName();
    }

    @RequestMapping("/user-config")
    public String userConfig() {
        return "Hello user userId= " + userConfigParam.getUserId() + ", userName=" + userConfigParam.getUserName();
    }

}