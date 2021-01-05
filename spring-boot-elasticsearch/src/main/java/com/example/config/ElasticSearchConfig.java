package com.example.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * ElasticSearch Rest client 配置
 *
 * @author 程序员小强
 */
@Configuration
public class ElasticSearchConfig {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Resource
    private ElasticSearchProperty elasticSearchProperty;

    @Bean
    public RestClientBuilder restClientBuilder() {
        Assert.notNull(elasticSearchProperty, "elasticSearchProperty cannot null ");
        Assert.notNull(elasticSearchProperty.getAddress(), "address hosts  cannot null ");

        //ElasticSearch 连接地址地址
        HttpHost[] httpHosts = this.getElasticSearchHttpHosts();

        return RestClient.builder(httpHosts).setRequestConfigCallback(requestConfigBuilder -> {
            //设置连接超时时间
            requestConfigBuilder.setConnectTimeout(elasticSearchProperty.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(elasticSearchProperty.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(elasticSearchProperty.getConnectionRequestTimeout());
            return requestConfigBuilder;
        }).setFailureListener(new RestClient.FailureListener() {
            //某节点失败，这里可以添加一些异常告警
            @Override
            public void onFailure(Node node) {
                log.error("[ ElasticSearchClient ] >>  node :{}, host:{},  fail ", node.getName(), node.getHost());
            }
        }).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            //设置账密
            return getHttpAsyncClientBuilder(httpClientBuilder);
        });
    }

    /**
     * ElasticSearch Rest client 配置
     *
     * @return RestHighLevelClient
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(@Qualifier("restClientBuilder") RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    /**
     * ElasticSearch 连接地址
     * 多个逗号分隔
     * 示例：127.0.0.1:9201,127.0.0.1:9202,127.0.0.1:9203
     */
    private HttpHost[] getElasticSearchHttpHosts() {
        String[] hosts = elasticSearchProperty.getAddress().split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < httpHosts.length; i++) {
            String host = hosts[i];
            host = host.replaceAll("http://", "").replaceAll("https://", "");
            Assert.isTrue(host.contains(":"), String.format("your host %s format error , Please refer to [ 127.0.0.1:9200 ] ", host));
            httpHosts[i] = new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]), "http");
        }
        return httpHosts;
    }

    private HttpAsyncClientBuilder getHttpAsyncClientBuilder(HttpAsyncClientBuilder httpClientBuilder) {
        if (StringUtils.isEmpty(elasticSearchProperty.getUserName()) || StringUtils.isEmpty(elasticSearchProperty.getPassword())) {
            return httpClientBuilder;
        }
        //账密设置
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //es账号密码（一般使用,用户elastic）
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticSearchProperty.getUserName(), elasticSearchProperty.getPassword()));
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        return httpClientBuilder;
    }
}

