package com.example.service;

import com.example.entity.UserInfo;
import com.example.web.bo.UserAddBO;
import com.example.web.bo.UserUpdateBO;

import java.util.List;

/**
 * 人员操作接口
 *
 * @author 程序员小强
 * @date 2020-07-26
 */
public interface UserService {

    /**
     * 新增人员
     *
     * @param addBO
     */
    void addUser(UserAddBO addBO);

    /**
     * 修改人员信息
     *
     * @param updateBO
     */
    void updateUser(UserUpdateBO updateBO);

    /**
     * 查询所有 人员信息
     *
     * @return 人员信息 列表
     */
    List<UserInfo> getAll();

    /**
     * 根据业务主键ID查询
     *
     * @param userId 业务主键
     */
    UserInfo getByUserId(String userId);



}
