package com.example.controller;

import com.example.util.WebToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class ConnectionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);

    @Autowired
    HttpServletRequest request;

    @Autowired
    Environment environment;

    @RequestMapping(value = {"/connection", "/test", "/demo", "/index"})
    public Object connection() throws Exception {
        String time = getDateFormat().format(new Date());
        String serverIp = WebToolUtils.getLocalIP();
        String osName = System.getProperty("os.name");
        String requestIp = WebToolUtils.getIpAddress(request);
        String serverPort = this.getServerPort();

        Map<String, Object> result = new LinkedHashMap<>();
        //成功标记
        result.put("success", true);
        //本机IP
        result.put("serverIp", serverIp);
        //服务端口
        result.put("serverPort", serverPort);
        //请求客户端IP
        result.put("requestIp", requestIp);
        //本机系统环境
        result.put("osName", osName);
        //当前时间
        result.put("time", time);

        LOGGER.info("[ {} ] >> 连通性测试结束  serverIp={} , serverPort={} , requestIp={}", time, serverIp, serverPort, requestIp);
        return result;
    }

    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String getServerPort() {
        return environment.getProperty("local.server.port");
    }

}