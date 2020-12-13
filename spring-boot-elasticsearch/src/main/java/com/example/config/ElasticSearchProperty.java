package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearch配置
 *
 * @author 程序员小强
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "config.elasticsearch")
public class ElasticSearchProperty {

    /**
     * 连接地址,格式：IP:端口
     * 多个逗号分隔
     * 示例：127.0.0.1:9201,127.0.0.1:9202,127.0.0.1:9203
     */
    private String address;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间
     * 默认10s
     */
    private int connectTimeout = 10000;

    /**
     * socket超时时间
     * 默认10s
     */
    private int socketTimeout = 10000;

    /**
     * 请求连接超时时间
     * 默认10s
     */
    private int connectionRequestTimeout = 10000;

}

