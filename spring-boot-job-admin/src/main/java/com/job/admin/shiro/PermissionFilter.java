package com.job.admin.shiro;

import com.alibaba.fastjson.JSON;
import com.job.admin.enums.ScheduledUserPowerEnum;
import com.job.admin.enums.SysExceptionEnum;
import com.job.admin.web.param.Response;
import com.job.admin.web.param.vo.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


/**
 * 权限认证过滤器
 *
 * @author mengq
 */
@Slf4j
public class PermissionFilter extends FormAuthenticationFilter {

    private static Set<String> allPermissionSet = ScheduledUserPowerEnum.getAllPower();

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
            log.debug("[权限验证] >> 从缓存获取权限不存在，需要重新登录 sessionId:{}", ShiroOperation.getSessionId());
            return false;
        }

        boolean isAccessAllowed = (!ObjectUtils.isEmpty(permissionSet) && permissionSet.contains(requestUrl));
        log.debug("[权限验证] >> username : {} , requestUrl : {} , isAccessAllowed : {}", currentUser.getUsername(), requestUrl, isAccessAllowed);
        return isAccessAllowed;
    }

    /**
     * isAccessAllowed 返回 false 时调用该方法，继续后续的操作
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse response) throws IOException {
        log.debug("[权限拒绝后验证] >> PermissionControlFilter >> onAccessDenied");

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setContentType("application/json;charset=utf-8");

        //拦截原因 > 默认权限不足
        SysExceptionEnum sysExceptionEnum = SysExceptionEnum.USER_ACCESS_DENIED;

        int httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (currentUser == null) {
            log.debug("[权限拒绝后验证] >> 用户未登录 ");
            sysExceptionEnum = SysExceptionEnum.SYSTEM_NOT_LOGIN_ERROR;
            httpStatus = HttpStatus.UNAUTHORIZED.value();
        }

        //请求地址(若访问不存在的页面直接跳转404)
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (!allPermissionSet.contains(httpServletRequest.getRequestURI())) {
            ((HttpServletResponse) response).sendRedirect("/404");
        }

        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(httpStatus);

            httpServletResponse.getWriter().write(JSON.toJSONString(Response.error(sysExceptionEnum.getCode(), sysExceptionEnum.getMsg())));
        } catch (Exception e) {
            log.error("[权限拒绝后验证] >> 写出提示信息出现异常 stack : {}", ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

}