package com.job.admin.service;

import com.job.admin.web.param.bo.*;
import com.job.admin.web.param.bo.JobTaskUserLoginBO;
import com.job.admin.web.param.vo.UserLoginResult;
import com.job.admin.web.param.vo.UserInfoDetailVO;
import com.job.admin.web.param.vo.UserInfoPageVO;

/**
 * 任务平台用户管理接口
 *
 * @author mengq
 */
public interface ScheduledQuartzUserService {

    /**
     * 用户登录
     *
     * @param loginParam
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
     */
    void saveUser(JobTaskUserSaveBO saveParam);

    /**
     * 修改用户
     *
     * @param updateBO
     */
    void updateUser(JobTaskUserUpdateBO updateBO);

    /**
     * 修改用户权限
     *
     * @param updateBO
     */
    void updateUserPower(JobTaskUserUpdatePowerBO updateBO);

    /**
     * 修改密码
     *
     * @param updateBO
     */
    void updatePwd(JobTaskUserUpdatePwdBO updateBO);

    /**
     * 删除用户
     *
     * @param operateBO
     */
    void deleteUser(JobTaskUserOperateBO operateBO);

}
