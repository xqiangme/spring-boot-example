package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据配置，进行初始化操作
 */
@Configuration
public class ElasticSearchConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Autowired
    private ElasticSearchProperty elasticSearchProperty;

    @Bean(name = "elasticSearchClient", initMethod = "init", destroyMethod = "close")
    public ElasticSearchClient elasticsearchClient() {
        return new ElasticSearchClient(elasticSearchProperty.getHost(), elasticSearchProperty.getPort(), elasticSearchProperty.getScheme());
    }


}

