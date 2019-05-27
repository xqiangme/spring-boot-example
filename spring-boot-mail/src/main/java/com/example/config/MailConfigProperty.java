package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置属性
 *
 * @author 码农猿
 */
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfigProperty {

    /**
     * 发件邮箱账户
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public MailConfigProperty setUsername(String username) {
        this.username = username;
        return this;
    }
}