package com.example.shiro;


import com.alibaba.fastjson.JSON;
import com.example.constant.MockConstant;
import com.example.model.result.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 身份认证realm
 */
@Slf4j
@Component
public class AuthRealm extends AuthorizingRealm {

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        log.info("[登录认证] 开始 >> 当前用户 username : {}", token.getUsername());


        //账户是否存在校验
        if (!MockConstant.MOCK_ACCOUNT_MAP.containsKey(token.getUsername())) {
            throw new UnknownAccountException();
        }

        String password = String.valueOf(token.getPassword());
        //密码校验
        if (!"123456".equals(password)) {
            throw new IncorrectCredentialsException();
        }

        //设置session 超时时间
        ShiroOperation.setSessionExpirationTime(ShiroConfig.SESSION_EXPIRATION_TIME);

        //获取用户拥有的权限(正式需从数据库获取)
        Set<String> permissionSet = MockConstant.MOCK_ACCOUNT_MAP.get(token.getUsername());

        //缓存权限
        ShiroOperation.setPermissions(permissionSet);

        //
        UserLoginResult result = new UserLoginResult();
        result.setUserId("1001");
        result.setUserName(token.getUsername());
        result.setPassword(password);

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
        log.info("[登录认证] 授权认证 >> 当前用户 username : {}", currentUser.getUserName());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }


}
