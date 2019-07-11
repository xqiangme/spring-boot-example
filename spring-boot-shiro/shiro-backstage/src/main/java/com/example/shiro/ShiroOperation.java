package com.example.shiro;

import com.example.constant.CommonConstant;
import com.example.model.result.UserLoginResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前用户各种变量获取和操作
 */
public class ShiroOperation {
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
        UserLoginResult user = (UserLoginResult) SecurityUtils.getSubject().getPrincipal();
        if (null != user) {
            //安全起见 抹除密码
            user.setPassword("***");
        }
        return user;
    }

    /**
     * 获取 sessionId
     */
    public static Serializable getSessionId() {
        Serializable id = SecurityUtils.getSubject().getSession().getId();
        return id;
    }


    /**
     * 缓存获取当前用户权限
     */
    public static Set<String> getPermissions() {
        Set<String> userPermissions =
                (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(CommonConstant.SHIRO_PERMISSIONKEY);
        return userPermissions;
    }

    /**
     * 设置权限缓存
     */
    public static void setPermissions(Set<String> userPermissions) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(CommonConstant.SHIRO_PERMISSIONKEY, userPermissions);
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
        return user.getUserName();
    }

    /**
     * 退出
     */
    public static void loginOut() {
        SecurityUtils.getSubject().logout();
    }


}
