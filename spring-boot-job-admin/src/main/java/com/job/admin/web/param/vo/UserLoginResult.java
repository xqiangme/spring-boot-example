package com.job.admin.web.param.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

@Slf4j
@Data
public class UserLoginResult implements Serializable {

    private static final long serialVersionUID = -4131001065560132027L;

    private Integer userId;
    private String username;
    private String realName;

    /**
     * 用户类型 1-超级管理员 2-管理员 3-普通用户
     */
    private Integer userType;

    /**
     * 用户类型层级
     */
    private Integer userTypeLevel;

    /**
     * 菜单集
     */
    private String menus;

    /**
     * 功能集
     */
    private String functions;

    /**
     * 登录IP
     */
    private String clientIp;

    /**
     * 登录IP实际地理位置
     */
    private String ipAddress;

    /**
     * 浏览器信息
     */
    private String browserName;

    /**
     * 系统信息
     */
    private String os;

    public String getCookieValue() {
        String cookieValue = "userId={0};username={1};realName={2};userType={3};menus={4};functions={5}";
        return MessageFormat.format(cookieValue, userId, username, realName, userType, menus, functions);
    }

    public String getUrlEncoderCookieValue() {
        try {
            return URLEncoder.encode(this.getCookieValue(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("cookie URLEncoder exception ", e);
        }
        return "";
    }

    public String getLogInfo() {
        String strValue = "userId: {0} , username: {1} , realName: {2}";
        return MessageFormat.format(strValue, userId, username, realName);
    }
}
