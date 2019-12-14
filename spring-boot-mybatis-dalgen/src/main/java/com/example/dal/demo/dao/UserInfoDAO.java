package com.example.dal.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.dal.demo.dataobject.UserInfoDO;
import java.util.List;
import com.example.dal.demo.mapper.UserInfoDOMapper;

/**
* The Table user_info.
* 用户表
*/
@Repository
public class UserInfoDAO{

    @Autowired
    private UserInfoDOMapper userInfoDOMapper;

    /**
     * desc:.<br/>
     * @return List<UserInfoDO>
     */
    public List<UserInfoDO> listAll(){
        return userInfoDOMapper.listAll();
    }
}
