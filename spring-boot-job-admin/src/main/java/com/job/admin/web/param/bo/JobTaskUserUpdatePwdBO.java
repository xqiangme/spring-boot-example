package com.job.admin.web.param.bo;

import lombok.Data;

@Data
public class JobTaskUserUpdatePwdBO extends JobBaseOperateBO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String newPassword;

}
