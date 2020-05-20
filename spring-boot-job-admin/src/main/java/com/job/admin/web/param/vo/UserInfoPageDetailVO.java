package com.job.admin.web.param.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mengq
 * @date 2020-04-17 18:23
 * @desc
 */
@Data
public class UserInfoPageDetailVO implements Serializable {

    private static final long serialVersionUID = -793494035658581853L;

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
     * 用户类型 1-超级管理员 2-管理员 3-普通用户
     */
    private Integer userType;

    /**
     * 用户类型等级
     */
    private Integer userTypeLevel;

    /**
     * 当前登录-用户等级
     */
    private Integer loginUserTypeLevel;

    private String createBy;
    private String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updateBy;
    private String updateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

}