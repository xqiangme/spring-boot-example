package com.example.sso.client2.interceptor;

import com.example.sso.client2.utils.CookieUtil;
import com.example.sso.client2.utils.SSOClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        log.info("[ WebInterceptor ] >> preHandle  requestUrl:{} ", request.getRequestURI());
        //判断是否有局部会话
        HttpSession session = request.getSession();
        Object isLogin = session.getAttribute("isLogin");
        if (isLogin != null && (Boolean) isLogin) {
            log.debug("[ WebInterceptor ] >> 已登录,有局部会话 requestUrl:{}", request.getRequestURI());
            return true;
        }
        //获取令牌ssoToken
        String token = SSOClientHelper.getSsoToken(request);

        //无令牌
        if (StringUtils.isEmpty(token)) {
            //认证中心验证是否已经登录(是否存在全局会话)
            SSOClientHelper.checkLogin(request, response);
            return true;
        }

        //有令牌-则请求认证中心校验令牌是否有效
        Boolean checkToken = SSOClientHelper.checkToken(token, session.getId());

        //令牌无效
        if (!checkToken) {
            log.debug("[ WebInterceptor ] >> 令牌无效,将跳转认证中心进行认证 requestUrl:{}, token:{}", request.getRequestURI(), token);
            //认证中心验证是否已经登录(是否存在全局会话)
            SSOClientHelper.checkLogin(request, response);
            return true;
        }

        //token有效，创建局部会话设置登录状态，并放行
        session.setAttribute("isLogin", true);
        //设置session失效时间-单位秒
        session.setMaxInactiveInterval(1800);
        //设置本域cookie
        CookieUtil.setCookie(response, SSOClientHelper.SSOProperty.TOKEN_NAME, token, 1800);
        log.debug("[ WebInterceptor ] >> 令牌有效,创建局部会话成功 requestUrl:{}, token:{}", request.getRequestURI(), token);
        return true;
    }

}