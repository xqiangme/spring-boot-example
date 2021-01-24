package com.example.sso.server.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 程序员小强
 * @date 2020-07-12 00:41
 */
@Slf4j
@Controller
public class IndexController {

    /**
     * 跳转index页
     */
    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }

    /**
     * 跳转登录页
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
