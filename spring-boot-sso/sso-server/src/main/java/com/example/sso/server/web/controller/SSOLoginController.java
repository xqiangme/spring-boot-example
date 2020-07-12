package com.example.sso.server.web.controller;

import com.example.sso.server.model.ClientRegisterModel;
import com.example.sso.server.model.SSOConstantPool;
import com.example.sso.server.web.param.LoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SSO 登录相关接口
 *
 * @author 程序员小强
 */
@Slf4j
@Controller
@RequestMapping("/sso")
public class SSOLoginController {


    /**
     * 认证中心SSO统一登录方法
     */
    @RequestMapping("/login")
    public String login(LoginParam loginParam, RedirectAttributes redirectAttributes,
                        HttpSession session, Model model) {

        //Demo 项目此处模拟数据库账密校验
        if (!"admin".equals(loginParam.getUsername()) || !"123456".equals(loginParam.getPassword())) {
            model.addAttribute("msg", "账户或密码错误，请重新登录!");
            model.addAttribute("redirectUrl", loginParam.getRedirectUrl());
            return "login";
        }

        //登录成功
        //创建令牌
        String ssoToken = UUID.randomUUID().toString();
        //把令牌放到全局会话中
        session.setAttribute("ssoToken", ssoToken);
        //设置session失效时间-单位秒
        session.setMaxInactiveInterval(3600);
        //将有效的令牌-放到map容器中(存在该容器中的token都是合法的,正式环境建议存库或者redis)
        SSOConstantPool.TOKEN_POOL.add(ssoToken);

        //未携带重定向跳转地址-默认跳转到认证中心首页
        if (StringUtils.isEmpty(loginParam.getRedirectUrl())) {
            return "index";
        }

        // 携带令牌到客户端
        redirectAttributes.addAttribute("ssoToken", ssoToken);
        log.info("[ SSO登录 ] login success ssoToken:{} , sessionId:{}", ssoToken, session.getId());
        // 跳转到客户端
        return "redirect:" + loginParam.getRedirectUrl();
    }

    /**
     * 校验令牌是否合法
     *
     * @param ssoToken    令牌
     * @param loginOutUrl 退出登录访问地址
     * @param jsessionid
     * @return 令牌是否有效
     */
    @ResponseBody
    @RequestMapping("/checkToken")
    public String verify(String ssoToken, String loginOutUrl, String jsessionid) {
        // 判断token是否存在map容器中,如果存在则代表合法
        boolean isVerify = SSOConstantPool.TOKEN_POOL.contains(ssoToken);
        if (!isVerify) {
            log.info("[ SSO-令牌校验 ] checkToken 令牌已失效 ssoToken:{}", ssoToken);
            return "false";
        }

        //把客户端的登出地址记录起来,后面注销的时候需要根据使用(生产环境建议存库或者redis)
        List<ClientRegisterModel> clientInfoList =
                SSOConstantPool.CLIENT_REGISTER_POOL.computeIfAbsent(ssoToken, k -> new ArrayList<>());
        ClientRegisterModel vo = new ClientRegisterModel();
        vo.setLoginOutUrl(loginOutUrl);
        vo.setJsessionid(jsessionid);
        clientInfoList.add(vo);
        log.info("[ SSO-令牌校验 ] checkToken success ssoToken:{} , clientInfoList:{}", ssoToken, clientInfoList);
        return "true";
    }

    /**
     * 校验是否已经登录认证中心（是否有全局会话）
     * 1.若存在则携带令牌ssoToken跳转至目标页面
     * 2.若不存在则跳转到登录页面
     */
    @RequestMapping("/checkLogin")
    public String checkLogin(String redirectUrl, RedirectAttributes redirectAttributes,
                             Model model, HttpServletRequest request) {
        //从认证中心-session中判断是否已经登录过(判断是否有全局会话)
        Object ssoToken = request.getSession().getAttribute("ssoToken");

        // ssoToken为空 - 没有全局回话
        if (StringUtils.isEmpty(ssoToken)) {
            log.info("[ SSO-登录校验 ] checkLogin fail 没有全局回话 ssoToken:{}", ssoToken);
            //登录成功需要跳转的地址继续传递
            model.addAttribute("redirectUrl", redirectUrl);
            //跳转到统一登录页面
            return "login";
        }

        log.info("[ SSO-登录校验 ] checkLogin success 有全局回话  ssoToken:{}", ssoToken);
        //重定向参数拼接（将会在url中拼接）
        redirectAttributes.addAttribute("ssoToken", ssoToken);
        //重定向到目标系统
        return "redirect:" + redirectUrl;
    }

    /**
     * 统一注销
     * 1.注销全局会话
     * 2.通过监听全局会话session时效性，向已经注册的所有子系统发起注销请求
     */
    @RequestMapping("/logOut")
    public String logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("[ SSO-统一退出 ] ....start.... sessionId:{}", session.getId());
        //注销全局会话, SSOSessionListener 监听器会处理后续操作
        request.getSession().invalidate();
        log.info("[ SSO-统一退出 ] ....end.... sessionId:{}", session.getId());
        return "logout";
    }
}