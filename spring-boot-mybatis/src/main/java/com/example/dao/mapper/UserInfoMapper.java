package com.example.dao.mapper;

import com.example.dao.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 由于需要对分页支持,请直接使用对应的DAO类
 * The Table user_info.
 * 用户表
 */
@Mapper
@Repository
public interface UserInfoMapper {

    /**
     * desc:.<br/>
     * descSql =  SELECT FROM user_info
     *
     * @return List<UserInfoDO>
     */
    List<UserInfo> listAll();
}
