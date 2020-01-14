package com.example.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据配置，进行初始化操作
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticSearch")
public class ElasticSearchProperty {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchProperty.class);

    private String host;
    private int port;
    private String scheme;
}

