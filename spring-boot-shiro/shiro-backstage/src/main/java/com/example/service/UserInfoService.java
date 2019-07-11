package com.example.service;

import com.example.model.param.UserLoginParam;
import com.example.model.result.UserLoginResult;

public interface UserInfoService {


    UserLoginResult login(UserLoginParam loginParam);
}
