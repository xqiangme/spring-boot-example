package com.example.sso.server.web.param;


import lombok.Data;

/**
 * 登录参数
 *
 * @author 程序员小强
 */
@Data
public class LoginParam {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 重定向地址
     */
    private String redirectUrl;

}
