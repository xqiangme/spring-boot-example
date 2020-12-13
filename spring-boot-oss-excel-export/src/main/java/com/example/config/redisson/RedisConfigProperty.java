package com.example.config.redisson;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis 配置属性类
 *
 * @author 程序员小强
 * @version RedisConfigProperty.java, v 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfigProperty {

    /**
     * Redis服务器地址
     */
    private String host;

    /**
     * Redis服务器连接密码（默认为空）
     */
    private String password;

    /**
     * Redis数据库索引（默认为0）
     */
    private int database;

    /**
     * Redis服务器连接端口
     */
    private int port;

    /**
     * 连接超时时间（毫秒）
     */
    private int timeout;
}
