package com.example.admin.web.param.vo;

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
     * 菜单集
     */
    private String menus;

    /**
     * 功能集
     */
    private String functions;

    public String getCookieValue() {
        String cookieValue = "userId={0};username={1};realName={2};menus={3};functions={4}";
        return MessageFormat.format(cookieValue, userId, username, realName, menus, functions);
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
