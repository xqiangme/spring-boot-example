package com.job.admin.web.param.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录参数
 *
 * @author mengq
 */
@Data
public class JobTaskUserLoginBO implements Serializable {

    private static final long serialVersionUID = 4220789951474952467L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码,需要加密
     */
    private String password;
    /**
     * 验证码
     */
    private String verifyCode;

}
