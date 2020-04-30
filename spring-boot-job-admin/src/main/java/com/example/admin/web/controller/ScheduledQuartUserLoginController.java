package com.example.admin.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alibaba.fastjson.JSON;
import com.example.admin.enums.SysExceptionEnum;
import com.example.admin.web.param.bo.JobTaskUserLoginBO;
import com.example.admin.web.param.Response;
import com.example.admin.web.param.vo.UserLoginResult;
import com.example.admin.service.ScheduledQuartUserService;
import com.example.admin.shiro.ShiroOperation;
import com.example.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户登录
 *
 * @author mengq
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class ScheduledQuartUserLoginController {

    @Resource
    private ScheduledQuartUserService scheduledQuartUserService;
    /**
     * cookie_key
     */
    public static final String COOKIE_USER_INFO = "COOKIE_USER_INFO";

    /**
     * 缓存验证码
     */
    public static final String CACHE_VERIFY_CODE_KEY = "CACHE_VERIFY_CODE_KEY";


    @RequestMapping("/getVerifyCode")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        log.debug("[request] getCaptcha");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 60);
        String verifyCode = lineCaptcha.getCode();
        log.info("生成验证码, sessionId={} , verifyCode={}", verifyCode, ShiroOperation.getSessionId());
        ShiroOperation.setCacheParam(CACHE_VERIFY_CODE_KEY, verifyCode);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //生成图片
            lineCaptcha.write(out);
        } finally {
            if (null != out) {
                out.close();
                log.info("生成验证码流关闭");
            }
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/in")
    public Response loginIn(@RequestBody JobTaskUserLoginBO loginParam, HttpServletResponse response) {
        log.info("[ LoginController ] >> 用户登录 loginParam:{}", JSON.toJSONString(loginParam));
        String verifyCode = (String) ShiroOperation.getCacheParam(CACHE_VERIFY_CODE_KEY);
        if (ObjectUtils.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(loginParam.getVerifyCode())) {
            throw new BusinessException(SysExceptionEnum.USER_CACHE_CODE_ERROR);
        }
        UserLoginResult result = scheduledQuartUserService.login(loginParam);
        //设置Cookie
        Cookie cookie = new Cookie(COOKIE_USER_INFO, result.getUrlEncoderCookieValue());
        //120分钟
        cookie.setMaxAge(120 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Response.success(scheduledQuartUserService.login(loginParam));
    }

    /**
     * 用户登出
     */
    @RequestMapping("/out")
    public Response loginOut(HttpServletRequest request, HttpServletResponse response) {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null == currentUser) {
            return Response.success();
        }
        log.info("[ LoginController ] >> 用户登出 currentUser:{}", currentUser.getLogInfo());
        ShiroOperation.loginOut();
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0) {
            return Response.success();
        }
        //清除cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_USER_INFO)) {
                cookie.setValue(null);
                //立即销毁cookie
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
        return Response.success();
    }

    /**
     * 获取登录用户信息
     */
    @RequestMapping("/getUserInfo")
    public Response getUserInfo() {
        Map<String, Object> map = new HashMap<>(2);
        //当前登录用户
        map.put("currentUser", ShiroOperation.getCurrentUser());
        //权限信息
        map.put("permissions", ShiroOperation.getPermissions());
        return Response.success(map);
    }

}
