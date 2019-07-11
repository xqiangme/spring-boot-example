package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.enums.SysExceptionEnum;
import com.example.exception.BusinessException;
import com.example.model.param.UserLoginParam;
import com.example.model.result.UserLoginResult;
import com.example.service.UserInfoService;
import com.example.shiro.ShiroOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {


    @Override
    public UserLoginResult login(UserLoginParam loginParam) {
        //验证码校验 todo
        //一些业务校验 ，失败次数，过期等校验 todo

        try {

            //密码处理
            String password = loginParam.getPassword();
            UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), password);
            //登陆拦截,权限相关操作
            SecurityUtils.getSubject().login(token);

        } catch (UnknownAccountException e) {
            log.error("[登录异常] >> 账户不存在 account : {}", JSON.toJSONString(loginParam));
            throw new BusinessException(SysExceptionEnum.USER_NOT_EXIST);
        } catch (IncorrectCredentialsException e) {
            log.error("[登录异常] >> 失败次数统计 account : {}", JSON.toJSONString(loginParam));
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
}
