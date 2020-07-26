package com.example.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员信息实体类
 *
 * @author 程序员小强
 * @date 2020-07-25
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    private Integer id;

    /**
     * 人员业务ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * remark
     */
    private String remark;

}
