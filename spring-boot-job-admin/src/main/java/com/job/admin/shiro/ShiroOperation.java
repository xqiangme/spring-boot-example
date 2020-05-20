package com.job.admin.shiro;

import com.job.admin.web.param.vo.UserLoginResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前用户信息获取
 *
 * @author mengq
 */
public class ShiroOperation {
    /**
     * shiro 权限缓存KEY
     */
    public static final String SHIRO_PERMISSIONKEY = "shiro_permissions";

    /**
     * 设置过期时间
     * 单位 ms,设置负数为永不超时
     */
    public static void setSessionExpirationTime(long expirationtime) {
        SecurityUtils.getSubject().getSession().setTimeout(expirationtime);
    }

    /**
     * 获取当前登录用户
     */
    public static UserLoginResult getCurrentUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (null == principal) {
            return null;
        }

        UserLoginResult user = (UserLoginResult) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 获取 sessionId
     */
    public static Serializable getSessionId() {
        return SecurityUtils.getSubject().getSession().getId();
    }


    /**
     * 缓存获取当前用户权限
     */
    public static Set<String> getPermissions() {
        Set<String> userPermissions =
                (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_PERMISSIONKEY);
        return userPermissions;
    }

    /**
     * 设置权限缓存
     */
    public static void setPermissions(Set<String> userPermissions) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(SHIRO_PERMISSIONKEY, userPermissions);
    }

    /**
     * 设置缓存
     */
    public static void setCacheParam(String key, Object value) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(key, value);
    }

    /**
     * 获取缓存
     */
    public static Object getCacheParam(String key) {
        Session session = SecurityUtils.getSubject().getSession();
        return session.getAttribute(key);
    }

    /**
     * 获取当前登录用户名
     */
    public static String getCurrentUserName() {
        UserLoginResult user = (UserLoginResult) SecurityUtils.getSubject().getPrincipal();
        return user.getUsername();
    }

    /**
     * 获取当前登录用户真实姓名
     */
    public static String getCurrentRealName() {
        UserLoginResult user = (UserLoginResult) SecurityUtils.getSubject().getPrincipal();
        return user.getRealName();
    }

    /**
     * 退出
     */
    public static void loginOut() {
        SecurityUtils.getSubject().logout();
    }


}
