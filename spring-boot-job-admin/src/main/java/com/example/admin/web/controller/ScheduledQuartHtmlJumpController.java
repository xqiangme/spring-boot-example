package com.example.admin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转
 *
 * @author mengq
 */
@Slf4j
@Controller
public class ScheduledQuartHtmlJumpController {

    @RequestMapping("/login-1")
    public ModelAndView login1() {
        return new ModelAndView("login-1");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping("/home")
    public ModelAndView welcome() {
        return new ModelAndView("home");
    }

    @RequestMapping("/404")
    public ModelAndView errorPage() {
        return new ModelAndView("404");
    }

    @RequestMapping("/user-list")
    public ModelAndView userList() {
        return new ModelAndView("user-list");
    }

    @RequestMapping("/user-detail")
    public ModelAndView userDetail() {
        return new ModelAndView("user-detail");
    }

    @RequestMapping("/user-add")
    public ModelAndView userAdd() {
        return new ModelAndView("user-add");
    }

    @RequestMapping("/user-person")
    public ModelAndView userPerson() {
        return new ModelAndView("user-person");
    }

    @RequestMapping("/user-edit-pwd")
    public ModelAndView useEditPwd() {
        return new ModelAndView("user-edit-pwd");
    }

    @RequestMapping("/user-edit-power")
    public ModelAndView userEditPower() {
        return new ModelAndView("user-edit-power");
    }

    @RequestMapping("/task-list")
    public ModelAndView taskList() {
        return new ModelAndView("task-list");
    }

    @RequestMapping("/task-add")
    public ModelAndView taskAdd() {
        return new ModelAndView("task-add");
    }

    @RequestMapping("/task-detail")
    public ModelAndView taskDetail() {
        return new ModelAndView("task-detail");
    }

    @RequestMapping("/task-log-list")
    public ModelAndView taskLogList() {
        return new ModelAndView("task-log-list");
    }

    @RequestMapping("/task-log-detail")
    public ModelAndView taskLogDetail() {
        return new ModelAndView("task-log-detail");
    }

}