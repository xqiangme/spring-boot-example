package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 码农猿
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public ModelAndView index(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if ("autoLogin".equals(name)) {
                String value = cookie.getValue();
                System.out.println("autoLogin >> value " + value);

                Object attribute = session.getAttribute(value);
                String sessionId = session.getId();

                System.out.println("session >> Id " + sessionId);
                System.out.println("session >> value " + attribute);
            }

        }
        model.addAttribute("welcome", "hello...欢迎回来。。。");
        return new ModelAndView("index");
    }


    @RequestMapping("/upload")
    public ModelAndView upload(Model model, HttpServletRequest request) {
        return new ModelAndView("upload");
    }

}