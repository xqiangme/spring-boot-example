package com.example.dao.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员信息实体类
 *
 * @author 程序员小强
 * @date 2020-12-14 11:22:34
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键id
     */
    private Integer id;

    /**
     * 人员id
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
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


}
