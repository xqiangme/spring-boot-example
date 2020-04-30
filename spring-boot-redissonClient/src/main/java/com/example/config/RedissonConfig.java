
package com.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mengqiang
 * @version .java, v 0.1   -   -
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisConfigProperty redisConfigProperty;

    @Bean(name = "redisson", destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisConfigProperty.getHost() + ":" + redisConfigProperty.getPort())
                .setPassword(redisConfigProperty.getPassword())
                .setDatabase(redisConfigProperty.getDatabase())
                .setFailedAttempts(3)
                .setPingTimeout(3000)
                .setTimeout(5000)
                .setSubscriptionConnectionMinimumIdleSize(1)
                .setSubscriptionConnectionPoolSize(256)
                .setConnectTimeout(redisConfigProperty.getTimeout())
                .setReconnectionTimeout(3000)
                .setConnectionPoolSize(256)
                .setConnectionMinimumIdleSize(1)
                .setRetryAttempts(3)
                .setRetryInterval(3000)
                .setIdleConnectionTimeout(30000);
        //添加主从配置
        return Redisson.create(config);
    }
    
}