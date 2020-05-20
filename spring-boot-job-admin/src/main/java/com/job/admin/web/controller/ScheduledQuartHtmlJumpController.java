package com.job.admin.web.controller;

import com.job.admin.config.BasicJobConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 页面跳转
 *
 * @author mengq
 */
@Slf4j
@Controller
public class ScheduledQuartHtmlJumpController {

    @Resource
    private BasicJobConfig basicJobConfig;

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("platformName", this.getPlatformName());
        return modelAndView;
    }

    @RequestMapping({"/", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("platformName", this.getPlatformName());
        return modelAndView;
    }

    @RequestMapping("/page/home")
    public ModelAndView welcome() {
        return new ModelAndView("home");
    }

    @RequestMapping("/404")
    public ModelAndView errorPage() {
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("platformName", this.getPlatformName());
        return modelAndView;
    }

    @RequestMapping("/page/user-list")
    public ModelAndView userList() {
        return new ModelAndView("user-list");
    }

    @RequestMapping("/page/user-detail")
    public ModelAndView userDetail() {
        return new ModelAndView("user-detail");
    }

    @RequestMapping("/page/user-add")
    public ModelAndView userAdd() {
        return new ModelAndView("user-add");
    }

    @RequestMapping("/page/user-person")
    public ModelAndView userPerson() {
        return new ModelAndView("user-person");
    }

    @RequestMapping("/page/user-edit-pwd")
    public ModelAndView useEditPwd() {
        return new ModelAndView("user-edit-pwd");
    }

    @RequestMapping("/page/user-edit-power")
    public ModelAndView userEditPower() {
        return new ModelAndView("user-edit-power");
    }

    @RequestMapping("/page/task-list")
    public ModelAndView taskList() {
        return new ModelAndView("task-list");
    }

    @RequestMapping("/page/task-add")
    public ModelAndView taskAdd() {
        return new ModelAndView("task-add");
    }

    @RequestMapping("/page/task-detail")
    public ModelAndView taskDetail() {
        return new ModelAndView("task-detail");
    }

    @RequestMapping("/page/task-corn")
    public ModelAndView taskCorn() {
        return new ModelAndView("task-corn");
    }

    @RequestMapping("/page/task-log-list")
    public ModelAndView taskLogList() {
        return new ModelAndView("task-log-list");
    }

    @RequestMapping("/page/task-log-detail")
    public ModelAndView taskLogDetail() {
        return new ModelAndView("task-log-detail");
    }

    /**
     * 平台名称
     *
     * @return
     */
    private String getPlatformName() {
        if (null == basicJobConfig.getPlatformName()) {
            return "";
        }
        //防乱码展示处理
        return new String(basicJobConfig.getPlatformName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}