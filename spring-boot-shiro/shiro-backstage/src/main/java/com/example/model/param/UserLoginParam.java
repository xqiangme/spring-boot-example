package com.example.model.param;

import com.example.model.base.BaseModel;
import lombok.Data;

@Data
public class UserLoginParam extends BaseModel {

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
