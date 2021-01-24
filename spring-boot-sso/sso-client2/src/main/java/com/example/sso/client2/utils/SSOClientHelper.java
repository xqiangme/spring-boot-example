package com.example.sso.client2.utils;

import cn.hutool.http.HttpUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 程序员小强
 * @date 2020-07-10 22:50
 */
public class SSOClientHelper {

    /**
     * SSO统一认证中心地址
     * http://localhost:8081
     */
    private static String SSO_SERVER_URL = "http://www.mysso.com:8081";

    /**
     * Client客户端地址
     */
    private static String CLIENT_SERVER_URL = "http://www.myclient2.com:8083";

    /**
     * 请求认证中心-校验登录或首次登录
     * <p>
     * 1.若存在全局会话则携带令牌重定向回客户端（已经登录）
     * 2.若无全局会话则返回统一登录页面进行登录（首次登录）
     *
     * @param request
     * @param response
     */
    public static void checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder();
        //获取当前客户端在访问的地址
        String redirectUrl = getCurrentServletPath(request);
        url.append(SSO_SERVER_URL)
                .append(SSOReqUrl.CHECK_LOGIN_URL)
                .append(redirectUrl);
        response.sendRedirect(url.toString());
    }

    /**
     * 请求认证中心-校验令牌token是否有效
     *
     * @param ssoToken   令牌 token
     * @param jsessionid
     * @return 是否有效
     */
    public static Boolean checkToken(String ssoToken, String jsessionid) {
        Map<String, Object> params = new HashMap<>(4);
        params.put(SSOProperty.TOKEN_NAME, ssoToken);
        params.put(SSOProperty.LOGIN_OUT_URL, SSOClientHelper.getClientLogOutUrl());
        params.put(SSOProperty.JSESSIONID, jsessionid);
        String checkToken = HttpUtil.get(SSOClientHelper.getCheckTokenUrl(), params);
        return "true".equals(checkToken);
    }

    /**
     * 获取 ssoToken
     * 1.优先从请求参数中获取
     * 2.二次尝试从cookie中获取
     */
    public static String getSsoToken(HttpServletRequest request) {
        //从参数中获取
        String token = request.getParameter(SSOProperty.TOKEN_NAME);
        //若参数未携带-尝试从cookie获取
        if (StringUtils.isEmpty(token)) {
            token = CookieUtil.getCookie(request, SSOProperty.TOKEN_NAME);
        }
        return token;
    }


    /**
     * 获取sso认证中心token校验请求地址
     * 示例：http://localhost:8081/sso/checkToken
     */
    public static String getCheckTokenUrl() {
        return SSO_SERVER_URL + SSOReqUrl.CHECK_TOKEN_URL;
    }

    /**
     * 获取SSO认证中心的登出地址
     * 示例：http://localhost:8081/logOut
     */
    public static String getSSOLogOutUrl() {
        return SSO_SERVER_URL + SSOReqUrl.SSO_LOGOUT_URL;
    }

    /**
     * 获取当前访问地址
     * 示例：http://localhost:8082/index
     *
     * @param request
     */
    public static String getCurrentServletPath(HttpServletRequest request) {
        return CLIENT_SERVER_URL + request.getServletPath();
    }

    /**
     * 获取客户端的登出地址
     * 示例：http://localhost:8082/logOut
     */
    public static String getClientLogOutUrl() {
        return CLIENT_SERVER_URL + ClientReqUrl.CLIENT_LOGOUT_URL;
    }

    /**
     * 认证中心-参数key 相关常量
     *
     * @author 程序员小强
     */
    public static class SSOProperty {
        /**
         * 令牌统一参数名
         */
        public static final String TOKEN_NAME = "ssoToken";
        /**
         * 统一认证中心的token认证方法的登出地址参数名
         */
        public static final String LOGIN_OUT_URL = "loginOutUrl";
        /**
         * 统一认证中心的token认证方法的jsessionid参数名
         */
        public static final String JSESSIONID = "jsessionid";
    }

    /**
     * 认证中心-请求URL 相关常量
     *
     * @author 程序员小强
     */
    public static class SSOReqUrl {
        /**
         * token校验地址
         */
        private static final String CHECK_TOKEN_URL = "/sso/checkToken";

        /**
         * 认证中心-退出登录地址
         */
        private static final String SSO_LOGOUT_URL = "/sso/logOut";

        /**
         * 登录校验地址
         */
        private static final String CHECK_LOGIN_URL = "/sso/checkLogin?redirectUrl=";
    }

    /**
     * 客户端-请求URL 相关常量
     *
     * @author 程序员小强
     */
    public static class ClientReqUrl {

        /**
         * 认证中心-退出登录地址
         */
        private static final String CLIENT_LOGOUT_URL = "/logOut";

    }

}
