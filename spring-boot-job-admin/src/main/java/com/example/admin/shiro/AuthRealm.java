package com.example.admin.shiro;


import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.example.admin.config.BasicJobConfig;
import com.example.admin.dao.mapper.ScheduledQuartzUserMapper;
import com.example.admin.dao.bean.ScheduledQuartUserInfo;
import com.example.admin.enums.ScheduledAdminFlagEnum;
import com.example.admin.enums.ScheduledUserPowerEnum;
import com.example.admin.enums.ScheduledUserStatusEnum;
import com.example.admin.web.param.vo.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 身份认证realm
 */
@Slf4j
@Component
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private BasicJobConfig basicJobConfig;

    @Autowired
    private ScheduledQuartzUserMapper scheduledQuartzUserMapper;

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        log.info("[登录认证] 开始 >> 当前用户 username : {}", username);

        ScheduledQuartUserInfo userInfo = scheduledQuartzUserMapper.getByProjectAndUsername(basicJobConfig.getProject(), token.getUsername());
        //账户不存在
        if (null == userInfo) {
            throw new UnknownAccountException();
        }

        String md5Pwd = SecureUtil.md5().digestHex(userInfo.getSalt() + password, "utf-8");
        //密码校验
        if (!userInfo.getPassword().equals(md5Pwd)) {
            throw new IncorrectCredentialsException();
        }
        //状态校验
        if (!userInfo.getUserStatus().equals(ScheduledUserStatusEnum.ENABLE.getValue())) {
            throw new DisabledAccountException();
        }

        //设置session 超时时间
        ShiroOperation.setSessionExpirationTime(ShiroConfig.SESSION_EXPIRATION_TIME);

        Set<String> permissionSet;
        String menus;
        String functions;
        //管理员拥有所有权限
        if (ScheduledAdminFlagEnum.ADMIN_FLAG.getValue().equals(userInfo.getAdminFlag())) {
            permissionSet = ScheduledUserPowerEnum.getAllPower();
            menus = ScheduledUserPowerEnum.getAllMenus();
            functions = ScheduledUserPowerEnum.getAllFunctions();
        } else {
            menus = userInfo.getMenus();
            functions = userInfo.getFunctions();
            //非管理员-获取用户权限
            permissionSet = ScheduledUserPowerEnum.getPowerByMenusAndFunctions(userInfo.getMenus(), userInfo.getFunctions());
        }
        //缓存权限
        ShiroOperation.setPermissions(permissionSet);
        UserLoginResult result = new UserLoginResult();
        result.setUserId(userInfo.getId());
        result.setUsername(userInfo.getUsername());
        result.setRealName(userInfo.getRealName());
        result.setMenus(menus);
        result.setFunctions(functions);

        log.info("[登录认证] 结束 >> 当前用户 result : {} , sessionId : {}", JSON.toJSONString(result), ShiroOperation.getSessionId());
        return new SimpleAuthenticationInfo(result, password, getName());
    }


    /**
     * 授权(验证权限时调用)
     * 现在采用过滤器所以该方法用不到
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null != currentUser) {
            log.info("[登录认证] 授权认证 >> 当前用户 username : {}", currentUser.getUsername());
        } else {
            log.error("[登录认证] 授权认证 >> 当前用户信息为空");
        }
        return new SimpleAuthorizationInfo();
    }


}
