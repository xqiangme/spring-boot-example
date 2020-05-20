package com.job.admin.web.param.bo;

import lombok.Data;

@Data
public class JobTaskUserSaveBO extends JobBaseOperateBO {

    /**
     * 用户名
     */
    private String username;

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
     * 备注
     */
    private String remarks;


}
