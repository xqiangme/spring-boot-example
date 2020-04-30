package com.example.admin.web.param.bo;

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
     * 管理员标记 1-管理员
     */
    private Integer adminFlag;

    /**
     * 备注
     */
    private String remarks;


}
