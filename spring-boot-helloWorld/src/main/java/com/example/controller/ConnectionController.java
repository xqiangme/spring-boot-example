package com.example.controller;

import com.example.util.WebToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private HttpServletRequest request;

    @RequestMapping(value = {"/connection", "/test", "/demo", "/index"})
    public Object connection() throws Exception {

        String time = getDateFormat().format(new Date());
        String localIp = WebToolUtils.getLocalIP();
        String osName = System.getProperty("os.name");
        String requestIp = WebToolUtils.getIpAddress(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("localIp", localIp);
        result.put("requestIp", requestIp);
        result.put("osName", osName);
        result.put("time", time);

        LOGGER.info("[ {} ] >> 连通性测试结束  localIP={} , requestIp={}", time, localIp, requestIp);
        return result;
    }

    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


}