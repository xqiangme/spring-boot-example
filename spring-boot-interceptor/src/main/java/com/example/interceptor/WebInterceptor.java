package com.example.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建自定义拦截器
 * 方法说明
 * preHandle():前置处理回调方法，返回true继续执行，返回false中断流程，不会继续调用其它拦截器
 * <p>
 * postHandle():后置处理回调方法，但在渲染视图之前
 * <p>
 * afterCompletion():全部后置处理之后，整个请求处理完毕后回调。
 */
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("[拦截器 WebInterceptor] >> {} >> 进入 preHandle 方法 ", request.getRequestURI());
        // TODO 前置业务处理

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("[拦截器 WebInterceptor] >> {} >> 进入 postHandle 方法 ", request.getRequestURI());
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("[拦截器 WebInterceptor] >> {} >> 进入 afterCompletion 方法 ", request.getRequestURI());
    }

}