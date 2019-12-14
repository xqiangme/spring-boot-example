package com.example.dal.demo.mapper;

import com.example.dal.demo.dataobject.UserInfoDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 由于需要对分页支持,请直接使用对应的DAO类
 * The Table user_info.
 * 用户表
 */
public interface UserInfoDOMapper{

    /**
     * desc:.<br/>
     * descSql =  SELECT FROM user_info
     * @return List<UserInfoDO>
     */
    List<UserInfoDO> listAll();
}
