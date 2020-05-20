package com.job.admin.shiro;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.job.admin.config.BasicJobConfig;
import com.job.admin.dao.mapper.ScheduledQuartzUserMapper;
import com.job.admin.dao.bean.ScheduledQuartUserInfo;
import com.job.admin.enums.ScheduledUserTypeEnum;
import com.job.admin.enums.ScheduledUserPowerEnum;
import com.job.admin.enums.ScheduledUserStatusEnum;
import com.job.admin.util.HttpServletContext;
import com.job.admin.util.IpAddressUtil;
import com.job.admin.web.param.vo.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 身份认证realm
 *
 * @author mengq
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

        ScheduledQuartUserInfo userInfo = scheduledQuartzUserMapper.getByProjectAndUsername(basicJobConfig.getProjectKey(), token.getUsername());
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
        if (ScheduledUserTypeEnum.isAdmin(userInfo.getUserType())) {
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
        result.setUserType(userInfo.getUserType());
        result.setUserTypeLevel(ScheduledUserTypeEnum.getLevelByType(userInfo.getUserType()));
        result.setMenus(menus);
        result.setFunctions(functions);
        //用户登录Ip地址等信息
        this.buildUserAddressInfo(result);

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

    /**
     * 处理用户IP地址等信息
     *
     * @param result
     */
    private void buildUserAddressInfo(UserLoginResult result) {
        try {
            // 获取request
            HttpServletRequest request = HttpServletContext.getHttpServletRequest();
            // 设置IP地址
            String clientIp = ServletUtil.getClientIP(request);
            result.setIpAddress("");
            if (IpAddressUtil.DEFAULT_IP.equals(clientIp)) {
                clientIp = IpAddressUtil.LOCAL_IP;
            }
            //IP
            result.setClientIp(clientIp == null ? "" : clientIp);
            // 设置浏览器和设备系统
            String header = request.getHeader("User-Agent");
            if (null == header) {
                return;
            }
            Browser browser = UserAgentUtil.parse(header).getBrowser();
            if (null != browser) {
                //浏览器信息
                result.setBrowserName(browser.getName() + " " + browser.getVersion(header));
            }
            Platform platform = UserAgentUtil.parse(header).getPlatform();
            if (null != platform) {
                //系统信息
                result.setOs(platform.getName());
            }
        } catch (Exception e) {
            log.error("[登录认证] 获取用户IP出现错误", e);
        }
    }

}
