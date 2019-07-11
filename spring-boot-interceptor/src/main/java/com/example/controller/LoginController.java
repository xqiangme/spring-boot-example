package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员管理接口
 *
 * @author 码农猿
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {


    /**
     * 登录
     *
     * @author 码农猿
     */
    @PostMapping(value = "/in")
    public String loginIn() {
        log.info("[业务处理] >> LoginController >> loginIn");
        return "success";
    }

}