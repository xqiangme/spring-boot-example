package com.example.shiro;

import com.example.config.RedisConfigProperty;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.*;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Configuration
public class ShiroConfig {

    public static final int EXPIRE = 1800;
    public static final int TIMEOUT = 20000;
    public static final long SESSION_EXPIRATION_TIME = EXPIRE * 1000;
    public static final String KEY_PREFIX = "demo.admin.session.";
    public static final String COOKIE_PREFIX = "demo.admin.cookie.";

    @Resource
    private RedisConfigProperty redisConfigProperty;

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean createShiroFilter(SecurityManager securityManager) {
        log.debug("[Shiro过滤器] >> 创建开始");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //登录地址 -> 若前后端分类，可能为空
        shiroFilterFactoryBean.setLoginUrl("/login");
        //无权限跳转地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //自定义认证过滤器
        Map<String, Filter> filterMap = Maps.newHashMap();
        //权限认证过滤器
        filterMap.put("authc", new PermissionFilter());

        shiroFilterFactoryBean.setFilters(filterMap);

        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/login/in", "anon");
        //登出
        filterChainDefinitionMap.put("/login/out", "anon");


        filterChainDefinitionMap.put("/shiro-msg/**", "anon");
        //拦截所有请求
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        log.debug("[Shiro过滤器] >> 创建结束");
        return shiroFilterFactoryBean;

    }

    @Bean
    public SecurityManager securityManager(AuthRealm authRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        //注入记住我管理器;
        //securityManager.setRememberMeManager(rememberMeManager());
        //身份认证realm.
        securityManager.setRealm(authRealm);
        return securityManager;
    }


    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisConfigProperty.getHost());
        redisManager.setPort(redisConfigProperty.getPort());
        redisManager.setPassword(redisConfigProperty.getPassword());
        redisManager.setDatabase(redisConfigProperty.getDatabase());
        //配置缓存过期时间
        redisManager.setExpire(EXPIRE);
        //超时时间
        redisManager.setTimeout(TIMEOUT);
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setKeyPrefix(KEY_PREFIX);
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }


    /**
     * 这里需要设置一个cookie的名称  原因就是会跟原来的session的id值重复的
     *
     * @return
     */
    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(COOKIE_PREFIX);
        return simpleCookie;
    }


    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
