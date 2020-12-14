package com.example.service;

import com.example.dao.bean.UserInfo;

import java.util.List;

/**
 * The Table user_info.
 * 用户表
 */
public interface UserInfoService {

    /**
     * desc:.<br/>
     *
     * @return List<UserInfoDO>
     */
    List<UserInfo> listAll();
}
