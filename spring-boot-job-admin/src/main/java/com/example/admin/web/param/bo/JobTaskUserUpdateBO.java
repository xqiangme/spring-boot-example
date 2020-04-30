package com.example.admin.web.param.bo;

import lombok.Data;

@Data
public class JobTaskUserUpdateBO extends JobBaseOperateBO {

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
     * 状态 1-启用;2-停用;3-锁定
     */
    private Integer userStatus;

    /**
     * 备注
     */
    private String remarks;
}
