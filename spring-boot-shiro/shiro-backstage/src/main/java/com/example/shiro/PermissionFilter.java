package com.example.shiro;

import com.alibaba.fastjson.JSON;
import com.example.constant.MockConstant;
import com.example.enums.SysExceptionEnum;
import com.example.model.result.Response;
import com.example.model.result.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 权限认证过滤器
 */
@Slf4j
public class PermissionFilter extends FormAuthenticationFilter {

    /**
     * 决定是否继续执行
     * 注：每一个请求都会拦截
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        log.debug("[权限验证] >> PermissionControlFilter >> isAccessAllowed sessionId:{}", ShiroOperation.getSessionId());
        if (!(servletRequest instanceof HttpServletRequest)) {
            return false;
        }
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (currentUser == null) {
            log.debug("[权限验证] >> 当前登录用户不存在 sessionId:{}", ShiroOperation.getSessionId());
            return false;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //请求地址 （注意：项目是否有访问统一前缀）
        String requestUrl = request.getRequestURI();

        //获取权限集 (正式项目一搬从缓存获取)
        Set<String> permissionSet;
        if (null != ShiroOperation.getPermissions()) {
            permissionSet = ShiroOperation.getPermissions();
            log.debug("[权限验证] >> 从缓存获取权限 size : {}", permissionSet.size());
        } else {
            //模拟从数据库获取
            permissionSet = MockConstant.MOCK_ACCOUNT_MAP.get(currentUser.getUserName());
            log.debug("[权限验证] >> 从数据库获取权限 size : {}", permissionSet.size());
        }

        boolean isAccessAllowed = (!ObjectUtils.isEmpty(permissionSet) && permissionSet.contains(requestUrl));
        log.debug("[权限验证] >> username : {} , requestUrl : {} , isAccessAllowed : {}", currentUser.getUserName(), requestUrl, isAccessAllowed);

        return isAccessAllowed;
    }


    /**
     * isAccessAllowed 返回 false 时调用该方法，继续后续的操作
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        log.debug("[权限拒绝后验证] >> PermissionControlFilter >> onAccessDenied");

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setContentType("application/json;charset=utf-8");

        //拦截原因 > 默认权限不足
        SysExceptionEnum sysExceptionEnum = SysExceptionEnum.USER_ACCESS_DENIED;

        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (currentUser == null) {
            sysExceptionEnum = SysExceptionEnum.SYSTEM_NOT_LOGIN_ERROR;
        }

//        //写出提示信息 >
//        //注：此处抛异常统一异常暂无法捕获，所以使用此方式输出异常信息
//        PrintWriter writer = null;
//        try {
//            writer = httpResponse.getWriter();
//            writer.write(JSON.toJSONString(Response.error(sysExceptionEnum.getCode(), sysExceptionEnum.getMsg())));
//            writer.flush();
//        } catch (Exception e) {
//            log.error("[权限拒绝后验证] >> 写出提示信息出现异常 stack : {}", ExceptionUtils.getStackTrace(e));
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }

        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(Response.error(sysExceptionEnum.getCode(), sysExceptionEnum.getMsg())));
        } catch (Exception e) {
            log.error("[权限拒绝后验证] >> 写出提示信息出现异常 stack : {}", ExceptionUtils.getStackTrace(e));
        } finally {

        }

        return false;
    }

}