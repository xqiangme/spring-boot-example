package com.example.admin.service;

import com.example.admin.web.param.bo.*;
import com.example.admin.web.param.bo.JobTaskUserLoginBO;
import com.example.admin.web.param.vo.UserLoginResult;
import com.example.admin.web.param.vo.UserInfoDetailVO;
import com.example.admin.web.param.vo.UserInfoPageVO;

/**
 * 任务平台用户管理接口
 *
 * @author mengq
 */
public interface ScheduledQuartUserService {

    /**
     * 登录
     *
     * @param loginParam
     * @return
     */
    UserLoginResult login(JobTaskUserLoginBO loginParam);

    /**
     * 分页列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    UserInfoPageVO listPage(JobTaskUserPageQueryBO queryBO);

    /**
     * 用户详情
     *
     * @param operateBO
     * @return 用户详情
     * @author mengq
     */
    UserInfoDetailVO getUserDetail(JobTaskUserOperateBO operateBO);

    /**
     * 个人详情
     *
     * @return 个人详情
     * @author mengq
     */
    UserInfoDetailVO getUserPersonDetail();


    /**
     * 新增用户
     *
     * @param saveParam
     * @return
     */
    void saveUser(JobTaskUserSaveBO saveParam);

    /**
     * 修改用户
     *
     * @param updateBO
     * @return
     */
    void updateUser(JobTaskUserUpdateBO updateBO);

    /**
     * 修改用户权限
     *
     * @param updateBO
     * @return
     */
    void updateUserPower(JobTaskUserUpdatePowerBO updateBO);

    /**
     * 修改密码
     *
     * @param updateBO
     * @return
     */
    void updatePwd(JobTaskUserUpdatePwdBO updateBO);

    /**
     * 删除用户
     *
     * @param operateBO
     * @return
     */
    void deleteUser(JobTaskUserOperateBO operateBO);

}
