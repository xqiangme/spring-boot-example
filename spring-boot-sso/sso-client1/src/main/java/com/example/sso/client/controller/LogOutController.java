package com.example.sso.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Slf4j
@Controller
public class LogOutController {

    @RequestMapping("/logOut")
    protected void logOut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        log.info("[ logOut ] sessionId:{}", session.getId());
        session.invalidate();
    }
}
