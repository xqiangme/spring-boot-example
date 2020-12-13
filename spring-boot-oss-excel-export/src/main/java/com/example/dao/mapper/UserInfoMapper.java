package com.example.dao.mapper;

import com.example.dao.entity.UserInfo;
import com.example.dao.query.UserInfoQuery;

import java.util.List;

/**
 * 测试 数据源 1
 * <p>
 * 人员信息mapper接口
 *
 * @author 程序员小强
 * @date 2019-03-25 23:08:13
 */
public interface UserInfoMapper {


    /**
     * 查询所有 人员信息
     *
     * @return 人员信息 列表
     * @author 程序员小强
     * @date 2019-03-25 23:08:13
     */
    List<UserInfo> listAll();

    /**
     * 根据业务主键id查询
     *
     * @param userId 业务主键
     * @return 人员信息实体
     * @author 程序员小强
     * @date 2019-03-25 23:08:13
     */
    UserInfo getByUserId(String userId);


    /**
     * 查询用户列表
     *
     * @param query 用户
     * @return 用户集合
     */
    int countByCondition(UserInfoQuery query);

    /**
     * 查询用户列表
     *
     * @param query 用户
     * @return 用户集合
     */
    List<UserInfo> listPageByCondition(UserInfoQuery query);


}
