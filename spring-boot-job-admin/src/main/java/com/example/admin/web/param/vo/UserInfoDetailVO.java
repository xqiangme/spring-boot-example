/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.admin.web.param.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mengq
 * @date 2020-04-17 18:23
 * @desc
 */
@Data
public class UserInfoDetailVO implements Serializable {

    private static final long serialVersionUID = 8644475177314757145L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 状态 1-启用，2-停用
     */
    private Integer userStatus;
    /**
     * 状态 1-启用，2-停用
     */
    private String userStatusStr;

    /**
     * 管理员标记 1-管理员
     */
    private Integer adminFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 菜单集
     */
    private String menus;

    /**
     * 功能集
     */
    private String functions;

    private String createBy;
    private String createName;

    private String createTimeStr;
    private Date createTime;

    private String updateBy;
    private String updateName;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    private Date updateTime;

}