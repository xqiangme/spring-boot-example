package com.example.demo.service.base;

import com.example.demo.bean.UserInfo;
import com.example.demo.param.UserListQueryBO;
import com.example.demo.param.UserListQueryBO2;

import java.util.List;

/**
 * 用户基本处理Service
 *
 * @author mengq
 * @version 1.0
 */
public interface UserCacheBaseService {

    /**
     * 无参数方法测试-查询全部用户
     *
     * @return
     */
    List<UserInfo> listAll();

    /**
     * 根据用户ID查询
     *
     * @param id
     * @return
     */
    UserInfo getUserById(Integer id);

    /**
     * 根据用户ID和状态查询
     *
     * @param id
     * @param status
     * @return
     */
    UserInfo getUserByIdAndStatus(Integer id, Integer status);

    /**
     * 根据用户ID查询-可存储null值
     *
     * @param id
     * @return
     */
    UserInfo getUserByIdStorageNull(Integer id);

    /**
     * 普通参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     * @return
     */
    List<UserInfo> listPageUser(Integer page, Integer pageSize);

    /**
     * 对象参数测试-分页查询用户
     *
     * @param queryBO
     * @return
     */
    List<UserInfo> listPageUserByObjParam(UserListQueryBO queryBO);

    /**
     * 普通参数+对象参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     * @param queryBO
     * @return
     */
    List<UserInfo> listPageUserByObjAndParam(Integer page, Integer pageSize, UserListQueryBO2 queryBO);
}
