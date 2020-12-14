package com.example.service.impl;

import com.example.dao.bean.UserInfo;
import com.example.dao.mapper.UserInfoMapper;
import com.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The Table user_info.
 * 用户表
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * desc:.<br/>
     *
     * @return List<UserInfoDO>
     */
    @Override
    public List<UserInfo> listAll() {
        return userInfoMapper.listAll();
    }
}
