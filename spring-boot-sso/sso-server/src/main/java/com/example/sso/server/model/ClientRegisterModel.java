package com.example.sso.server.model;

import lombok.Data;

/**
 * 客户端注册model
 *
 * @author 程序员小强
 */
@Data
public class ClientRegisterModel {

    /**
     * 客户端-退出登录地址
     */
    private String loginOutUrl;

    /**
     * 客户端-jsessionid
     */
    private String jsessionid;

}
