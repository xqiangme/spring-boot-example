package com.example.sso.server.listener;


import cn.hutool.http.HttpUtil;
import com.example.sso.server.model.ClientRegisterModel;
import com.example.sso.server.model.SSOConstantPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * session监听器
 *
 * @author 程序员小强
 */
@Slf4j
@WebListener
public class SSOSessionListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    /**
     * 销毁事件监听
     * <p>
     * 1.session超时的时候会调用
     * 2.手动调用session.invalidate()方法时会调用.
     *
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String token = (String) session.getAttribute("ssoToken");
        log.debug("[ SSOSessionListener ] ...start..... sessionId:{},token:{}", session.getId(), token);
        //注销全局会话,SSOSessionListener监听类删除对应的信息
        session.invalidate();
        if (StringUtils.isEmpty(token)) {
            log.debug("[ SSOSessionListener ] token is null sessionId:{}", session.getId());
            return;
        }
        //清除存储的有效token数据
        SSOConstantPool.TOKEN_POOL.remove(token);
        //清除并返回已经注册的系统信息
        List<ClientRegisterModel> clientRegisterList = SSOConstantPool.CLIENT_REGISTER_POOL.remove(token);
        if (CollectionUtils.isEmpty(clientRegisterList)) {
            return;
        }
        for (ClientRegisterModel client : clientRegisterList) {
            if (null == client) {
                continue;
            }
            //取出注册的子系统，依次调用子系统的登出方法(通过会话ID退出子系统的局部会话)
            sendHttpRequest(client.getLoginOutUrl(), client.getJsessionid());
            log.info("[ SSOSessionListener ] 注销系统 url:{},Jsessionid:{}", client.getLoginOutUrl(), client.getJsessionid());
        }
        log.debug("[ SSOSessionListener ] ...end..... sessionId:{},token:{}", session.getId(), token);
    }

    /**
     * 发送退出登录请求
     * 模拟浏览器访问形式
     *
     * @param reqUrl     发送请求的地址
     * @param jesssionId 会话Id
     */
    private static void sendHttpRequest(String reqUrl, String jesssionId) {
        try {
            //建立URL连接对象
            URL url = new URL(reqUrl);
            //创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求的方式(需要是大写的)
            conn.setRequestMethod("POST");
            //设置需要响应结果
            conn.setDoOutput(true);
            //通过设置JSESSIONID模拟浏览器端操作
            conn.addRequestProperty("Cookie", "JSESSIONID=" + jesssionId);
            //发送请求到服务器
            conn.connect();
            conn.getInputStream();
            conn.disconnect();
        } catch (Exception e) {
            log.error("[ sendHttpRequest ] exception >> reqUrl:{}", reqUrl, e);
        }
    }
}
