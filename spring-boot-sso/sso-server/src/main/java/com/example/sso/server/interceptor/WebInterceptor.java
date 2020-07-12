package com.example.sso.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建拦截器-拦截需要安全访问的请求
 * 方法说明
 * 1.preHandle():前置处理回调方法，返回true继续执行，返回false中断流程，不会继续调用其它拦截器
 * 2.postHandle():后置处理回调方法，但在渲染视图之前
 * 3.afterCompletion():全部后置处理之后，整个请求处理完毕后回调。
 *
 * @author 程序员小强
 */
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.debug("[ WebInterceptor ] >> preHandle start requestUrl:{} ", request.getRequestURI());
        //从认证中心-session中判断是否已经登录过(判断是否有全局会话)
        Object ssoToken = request.getSession().getAttribute("ssoToken");
        // ssoToken为空 - 没有全局回话
        if (StringUtils.isEmpty(ssoToken)) {
            log.debug("[ WebInterceptor ] >> preHandle check fail need login  requestUrl:{}", request.getRequestURI());
            //未登录认证中心-跳转到登录页面
            response.sendRedirect("/login");
        }

        log.debug("[ WebInterceptor ] >> preHandle end  requestUrl:{},ssoToken:{}", request.getRequestURI(), ssoToken);
        return true;
    }

}