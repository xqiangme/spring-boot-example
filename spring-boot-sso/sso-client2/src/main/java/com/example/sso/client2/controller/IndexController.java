package com.example.sso.client2.controller;

import com.example.sso.client2.utils.SSOClientHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"/", "/index"})
    protected String index(Model model) {
        model.addAttribute("logOutUrl", SSOClientHelper.getSSOLogOutUrl());
        return "index";
    }

    @RequestMapping("/welcome")
    protected String welcome(Model model) {
        return "welcome";
    }

}
