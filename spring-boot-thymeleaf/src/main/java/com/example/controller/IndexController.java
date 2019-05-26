package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 码农猿
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public ModelAndView index(Model model) {
        model.addAttribute("welcome", "hello...欢迎回来。。。");
        return new ModelAndView("index");
    }


}