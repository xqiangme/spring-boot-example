package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/log")
public class ApolloLogTestController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloLogTestController.class);

    @GetMapping("/test")
    public void logTest() {
        LOGGER.info("info log 。。。==> {}", LOGGER.isInfoEnabled());
        LOGGER.debug("debug log 。。。==> {}", LOGGER.isDebugEnabled());
        LOGGER.warn("warn log 。。。==> {}", LOGGER.isWarnEnabled());
        LOGGER.error("error log 。。。==> {}", LOGGER.isErrorEnabled());
    }

}