package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author 程序员小强
 */
@Data
public class UserInfoModel implements Serializable {

    private static final long serialVersionUID = -5834032902052568300L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 备注
     */
    private String remarks;
}