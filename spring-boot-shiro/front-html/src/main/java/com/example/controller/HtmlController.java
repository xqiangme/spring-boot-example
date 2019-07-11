package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HtmlController {


    @RequestMapping("/login")
    public String login() {
        log.info("[页面跳转] >> login ");
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        log.info("[页面跳转] >> index ");
        return "index";
    }

    @RequestMapping("/userList")
    public String userList() {
        log.info("[页面跳转] >> userList ");
        return "/userList";
    }

    @RequestMapping("/userAdd")
    public String userAdd() {
        log.info("[页面跳转] >> userAdd ");
        return "/userAdd";
    }

    @RequestMapping("/userDel")
    public String userDel() {
        log.info("[页面跳转] >> userDel ");
        return "/userDel";
    }

    @RequestMapping("/userDetail")
    public String userDetail() {
        log.info("[页面跳转] >> userDetail ");
        return "/userDetail";
    }

    @RequestMapping("/login-auth")
    public String loginauth() {
        log.info("[页面跳转] >> login-auth ");
        return "login-auth";
    }

}