package com.job.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.job.admin.config.BasicJobConfig;
import com.job.admin.dao.mapper.ScheduledQuartzUserMapper;
import com.job.admin.dao.bean.ScheduledQuartUserInfo;
import com.job.admin.enums.ScheduledDelFlagEnum;
import com.job.admin.enums.ScheduledUserStatusEnum;
import com.job.admin.enums.ScheduledUserTypeEnum;
import com.job.admin.enums.SysExceptionEnum;
import com.job.config.exception.BusinessException;
import com.job.admin.web.param.bo.*;
import com.job.admin.dao.dto.JobUserPageQueryDTO;
import com.job.admin.web.param.bo.JobTaskUserLoginBO;
import com.job.admin.web.param.vo.UserLoginResult;
import com.job.admin.web.param.vo.*;
import com.job.admin.service.ScheduledQuartzUserService;
import com.job.admin.shiro.ShiroOperation;
import com.job.admin.util.BeanCopierUtil;
import com.job.admin.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务平台用户管理接口
 *
 * @author mengq
 */
@Slf4j
@Service
public class ScheduledQuartzUserServiceImpl implements ScheduledQuartzUserService {

    @Resource
    private BasicJobConfig basicJobConfig;

    @Resource
    private ScheduledQuartzUserMapper scheduledQuartzUserMapper;

    private final ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 用户登录
     *
     * @param loginParam
     * @return
     */
    @Override
    public UserLoginResult login(JobTaskUserLoginBO loginParam) {
        try {
            String password = loginParam.getPassword();
            UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), password);
            //登陆拦截,权限相关操作
            SecurityUtils.getSubject().login(token);
            //更新当前用户最后登录时间
            this.updateLastLoginTime();
        } catch (UnknownAccountException e) {
            log.error("[登录异常] >> 账户不存在 account : {}", JSON.toJSONString(loginParam));
            throw new BusinessException(SysExceptionEnum.USER_NOT_EXIST);
        } catch (IncorrectCredentialsException e) {
            log.error("[登录异常] >> 用户名或密码错误 account : {}", JSON.toJSONString(loginParam));
            throw new BusinessException(SysExceptionEnum.USER_PASSWORD_ERROR);
        } catch (LockedAccountException e) {
            log.error("[登录异常] >> 账户已锁定 account : {}", JSON.toJSONString(loginParam));
            throw new BusinessException(SysExceptionEnum.USER_LOGIN_ERROR);
        } catch (DisabledAccountException e) {
            log.error("[登录异常] >> 账户已停用 account : {}", JSON.toJSONString(loginParam));
            throw new BusinessException(SysExceptionEnum.USER_LOGIN_ERROR);
        } catch (Exception e) {
            log.error("[登录异常] >> 其它登录异常 account : {}", JSON.toJSONString(loginParam), e);
            throw new BusinessException(SysExceptionEnum.USER_LOGIN_ERROR);
        }
        return ShiroOperation.getCurrentUser();
    }

    /**
     * 分页列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    @Override
    public UserInfoPageVO listPage(JobTaskUserPageQueryBO queryBO) {
        String projectKey = this.getProjectKeyKey();

        //构建查询参数
        JobUserPageQueryDTO pageQueryDTO = JobUserPageQueryDTO.builder()
                .projectKey(projectKey)
                .limit(PageUtils.getStartRow(queryBO.getPage(), queryBO.getLimit()))
                .pageSize(PageUtils.getOffset(queryBO.getLimit()))
                .userStatus(queryBO.getUserStatus())
                .userType(queryBO.getUserType())
                .usernameLike(queryBO.getUsernameLike())
                .realNameLike(queryBO.getRealNameLike())
                .build();

        Integer total = scheduledQuartzUserMapper.countByCondition(pageQueryDTO);
        if (total <= 0) {
            return UserInfoPageVO.initDefault();
        }

        List<ScheduledQuartUserInfo> userList = scheduledQuartzUserMapper.listPageByCondition(pageQueryDTO);
        if (CollectionUtils.isEmpty(userList)) {
            return new UserInfoPageVO(total, new ArrayList<>(0));
        }

        return new UserInfoPageVO(total, this.buildPageResultList(userList));
    }

    /**
     * 用户详情
     *
     * @param operateBO
     * @return 用户详情
     * @author mengq
     */
    @Override
    public UserInfoDetailVO getUserDetail(JobTaskUserOperateBO operateBO) {
        ScheduledQuartUserInfo userInfo = this.getUserById(operateBO.getId());
        return BeanCopierUtil.copy(userInfo, UserInfoDetailVO.class);
    }

    /**
     * 个人详情
     *
     * @return 个人详情
     * @author mengq
     */
    @Override
    public UserInfoDetailVO getUserPersonDetail() {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(SysExceptionEnum.SYSTEM_NOT_LOGIN_ERROR);
        }
        ScheduledQuartUserInfo userInfo = this.getUserById(currentUser.getUserId());
        UserInfoDetailVO userInfoDetail = BeanCopierUtil.copy(userInfo, UserInfoDetailVO.class);
        userInfoDetail.setUserStatusStr(ScheduledUserStatusEnum.getByValue(userInfo.getUserStatus()).getName());
        userInfoDetail.setLastLoginTime("");
        if (userInfo.getLastLoginTimestamp() > 0) {
            userInfoDetail.setLastLoginTime(DateUtil.formatDateTime(new Date(userInfo.getLastLoginTimestamp())));
        }
        return userInfoDetail;
    }

    /**
     * 新增用户
     *
     * @param saveParam
     */
    @Override
    public void saveUser(JobTaskUserSaveBO saveParam) {
        String projectKey = basicJobConfig.getProjectKey();
        reentrantLock.lock();
        try {
            String username = saveParam.getUsername();
            //校验是否存在
            int count = scheduledQuartzUserMapper.countByProjectAndUsername(projectKey, username);
            if (count > 0) {
                //用户已经存在
                throw new BusinessException(SysExceptionEnum.SAME_USER_NAME_EXISTS);
            }
            String salt = UUID.randomUUID().toString().replace("-", "");
            ScheduledQuartUserInfo userInfo = new ScheduledQuartUserInfo();
            userInfo.setProjectKey(projectKey);
            userInfo.setUsername(username);
            userInfo.setRealName(saveParam.getRealName());
            userInfo.setUserType(saveParam.getUserType());
            userInfo.setUserStatus(ScheduledUserStatusEnum.ENABLE.getValue());
            userInfo.setCreateBy(saveParam.getOperateBy());
            userInfo.setCreateName(saveParam.getOperateName());
            userInfo.setUpdateBy(saveParam.getOperateBy());
            userInfo.setUpdateName(saveParam.getOperateName());
            userInfo.setRemarks(saveParam.getRemarks());
            userInfo.setSalt(salt);
            userInfo.setPassword(SecureUtil.md5().digestHex(salt + saveParam.getPassword(), "utf-8"));
            scheduledQuartzUserMapper.addUser(userInfo);
            log.info("[ ScheduledQuartUserServiceImpl ] saveUser success saveParam:{}", JSON.toJSONString(saveParam));
        } finally {
            reentrantLock.unlock();
            log.debug("新增用户 , 释放锁>> saveUser >> param={}", JSON.toJSONString(saveParam));
        }
    }

    /**
     * 修改用户
     *
     * @param updateBO
     */
    @Override
    public void updateUser(JobTaskUserUpdateBO updateBO) {
        String project = basicJobConfig.getProjectKey();
        String username = updateBO.getUsername();
        reentrantLock.lock();
        try {
            ScheduledQuartUserInfo userInfo = scheduledQuartzUserMapper.getById(updateBO.getId());
            if (null == userInfo) {
                throw new BusinessException(SysExceptionEnum.USER_NOT_EXIST, username);
            }
            //校验用户名否重复
            int count = scheduledQuartzUserMapper.countByProjectAndUsernameExcludeId(project, username, updateBO.getId());
            if (count > 0) {
                throw new BusinessException(SysExceptionEnum.SAME_USER_NAME_EXISTS, username);
            }
            ScheduledQuartUserInfo updateUserInfo = new ScheduledQuartUserInfo();
            updateUserInfo.setId(updateBO.getId());
            updateUserInfo.setUserStatus(updateBO.getUserStatus());
            updateUserInfo.setRealName(updateBO.getRealName());
            updateUserInfo.setUsername(updateBO.getUsername());
            updateUserInfo.setUserType(updateBO.getUserType());
            updateUserInfo.setUpdateBy(updateBO.getOperateBy());
            updateUserInfo.setUpdateName(updateBO.getOperateName());
            updateUserInfo.setRemarks(StringUtils.isBlank(updateBO.getRemarks()) ? "" : updateBO.getRemarks());
            scheduledQuartzUserMapper.updateSelectiveById(updateUserInfo);
            log.info("[ ScheduledQuartUserServiceImpl ] updateUser success saveParam:{}", JSON.toJSONString(updateBO));
        } finally {
            reentrantLock.unlock();
            log.debug("修改用户 , 释放锁>> updateUser >> param={}", JSON.toJSONString(updateBO));
        }
    }

    /**
     * 修改用户权限
     *
     * @param updateBO
     */
    @Override
    public void updateUserPower(JobTaskUserUpdatePowerBO updateBO) {
        //校验用户是否存在
        this.getUserById(updateBO.getId());

        ScheduledQuartUserInfo updateUserInfo = new ScheduledQuartUserInfo();
        updateUserInfo.setId(updateBO.getId());

        StringBuilder menusBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(updateBO.getMenus())) {
            for (String menu : updateBO.getMenus()) {
                menusBuilder.append(",").append(menu);
            }
        }
        StringBuilder functionsBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(updateBO.getFunctions())) {
            for (String function : updateBO.getFunctions()) {
                functionsBuilder.append(",").append(function);
            }
        }
        String menus = String.valueOf(menusBuilder);
        String functions = String.valueOf(functionsBuilder);
        updateUserInfo.setMenus(StringUtils.isBlank(menus) ? "" : menus.substring(1));
        updateUserInfo.setFunctions(StringUtils.isBlank(functions) ? "" : functions.substring(1));
        updateUserInfo.setUpdateBy(updateBO.getOperateBy());
        updateUserInfo.setUpdateName(updateBO.getOperateName());
        scheduledQuartzUserMapper.updateSelectiveById(updateUserInfo);
        log.info("[ ScheduledQuartUserServiceImpl ] updateUserPower success saveParam:{}", JSON.toJSONString(updateBO));
    }

    /**
     * 修改密码
     *
     * @param updateBO
     */
    @Override
    public void updatePwd(JobTaskUserUpdatePwdBO updateBO) {
        ScheduledQuartUserInfo userInfo = this.getUserById(updateBO.getId());

        ScheduledQuartUserInfo userPwdInfo = new ScheduledQuartUserInfo();
        userPwdInfo.setId(updateBO.getId());
        userPwdInfo.setPassword(SecureUtil.md5().digestHex(userInfo.getSalt() + updateBO.getNewPassword(), "utf-8"));
        userPwdInfo.setUpdateBy(updateBO.getOperateBy());
        userPwdInfo.setUpdateName(updateBO.getOperateName());
        scheduledQuartzUserMapper.updateSelectiveById(userPwdInfo);
        log.info("[ ScheduledQuartUserServiceImpl ] updatePwd success id:{},operateName:{}", updateBO.getId(), updateBO.getOperateName());
    }

    /**
     * 删除用户
     *
     * @param operateBO
     */
    @Override
    public void deleteUser(JobTaskUserOperateBO operateBO) {
        //校验用户是否存在
        this.getUserById(operateBO.getId());

        ScheduledQuartUserInfo configInfo = new ScheduledQuartUserInfo();
        configInfo.setId(operateBO.getId());
        configInfo.setUpdateBy(operateBO.getOperateBy());
        configInfo.setUpdateName(operateBO.getOperateName());
        configInfo.setDelFlag(ScheduledDelFlagEnum.DELETE.getValue());

        scheduledQuartzUserMapper.updateSelectiveById(configInfo);
        log.info("[ ScheduledQuartUserServiceImpl ] deleteUser success id:{},operateName:{}", operateBO.getId(), operateBO.getOperateName());
    }

    /**
     * 构建分页结果集
     *
     * @param userList
     * @return
     */
    private List<UserInfoPageDetailVO> buildPageResultList(List<ScheduledQuartUserInfo> userList) {
        UserInfoPageDetailVO detailVO = null;
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        Map<Integer, ScheduledUserTypeEnum> allType = ScheduledUserTypeEnum.getAllType();
        List<UserInfoPageDetailVO> resultList = new ArrayList<>(userList.size());
        for (ScheduledQuartUserInfo userInfo : userList) {
            detailVO = BeanCopierUtil.copy(userInfo, UserInfoPageDetailVO.class);
            detailVO.setLastLoginTime("");
            if (userInfo.getLastLoginTimestamp() > 0) {
                detailVO.setLastLoginTime(DateUtil.formatDateTime(new Date(userInfo.getLastLoginTimestamp())));
            }
            if (allType.containsKey(userInfo.getUserType())) {
                detailVO.setUserTypeLevel(allType.get(userInfo.getUserType()).getLevel());
            }
            if (null != currentUser) {
                detailVO.setLoginUserTypeLevel(currentUser.getUserTypeLevel() == null ? 0 : currentUser.getUserTypeLevel());
            }
            resultList.add(detailVO);
        }
        return resultList;
    }

    /**
     * 更新当前用户最后登录时间
     */
    private void updateLastLoginTime() {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(SysExceptionEnum.SYSTEM_NOT_LOGIN_ERROR);
        }
        ScheduledQuartUserInfo updateUser = new ScheduledQuartUserInfo();
        updateUser.setId(currentUser.getUserId());
        updateUser.setLastLoginTimestamp(System.currentTimeMillis());
        scheduledQuartzUserMapper.updateSelectiveById(updateUser);
    }

    /**
     * 根据ID获取用户
     */
    private ScheduledQuartUserInfo getUserById(Integer id) {
        ScheduledQuartUserInfo userInfo = scheduledQuartzUserMapper.getById(id);
        if (null == userInfo) {
            throw new BusinessException(SysExceptionEnum.USER_NOT_EXIST);
        }

        return userInfo;
    }

    /**
     * 获取任务key
     */
    private String getProjectKeyKey() {
        String projectKey = basicJobConfig.getProjectKey();
        if (StringUtils.isBlank(projectKey)) {
            throw new BusinessException(SysExceptionEnum.PROJECT_NOT_EXISTS);
        }
        return projectKey;
    }
}
