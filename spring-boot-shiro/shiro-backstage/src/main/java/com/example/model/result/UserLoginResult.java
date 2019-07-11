package com.example.model.result;

import com.example.model.base.BaseModel;
import lombok.Data;

@Data
public class UserLoginResult extends BaseModel {

    private String userId;
    private String roleId;
    private String userName;
    private String password;

}
