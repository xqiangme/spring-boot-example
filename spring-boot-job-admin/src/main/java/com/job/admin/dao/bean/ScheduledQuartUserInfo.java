package com.job.admin.dao.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务平台用户表
 *
 * @author mengq
 */
@Data
public class ScheduledQuartUserInfo implements Serializable {

    private static final long serialVersionUID = -3437989904588702812L;

    /**
     * 配置ID
     */
    private Integer id;

    /**
     * 项目
     */
    private String projectKey;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户类型 1-超级管理员 2-管理员 3-普通用户
     */
    private Integer userType;

    /**
     * 状态 1-启用;2-停用;3-锁定
     */
    private Integer userStatus;

    /**
     * 菜单集
     */
    private String menus;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 功能集
     */
    private String functions;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 最后台修改人ID
     */
    private String updateBy;

    /**
     * 最后修改人名称
     */
    private String updateName;

    /**
     * 最后登录时间戳
     */
    private Long lastLoginTimestamp;

    /**
     * 删除标记
     */
    private Integer delFlag;


}
