package com.example.listener;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Apollo 配置监听
 */
@Configuration
public class ApolloConfigListener implements ApplicationContextAware {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloConfigListener.class);
    /**
     * 日志配置常量
     */
    private static final String LOGGER_TAG = "logging.level.";

    @Resource
    private LoggingSystem loggingSystem;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 配置监听
     * ApolloConfigChangeListener > value 属性默认 命名空间 "application"
     *
     * 示例： @ApolloConfigChangeListener(value = {"application", "test_space"})
     */
    @ApolloConfigChangeListener(value = {"application", "test_space"})
    private void onChangeConfig(ConfigChangeEvent changeEvent) {
        LOGGER.info("【Apollo-config-change】>> start");
        for (String key : changeEvent.changedKeys()) {
            ConfigChange change = changeEvent.getChange(key);
            LOGGER.info("【Apollo-config-change】>> key={} , propertyName={} , oldValue={} , newValue={} ",
                    key, change.getPropertyName(), change.getOldValue(), change.getNewValue());
            //是否为日志配置
            if (StringUtils.containsIgnoreCase(key, LOGGER_TAG)) {
                //日志配置刷新
                changeLoggingLevel(key, change);
                continue;
            }
            // 更新相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean
            this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        }
        LOGGER.info("【Apollo-config-change】>> end");
    }

    /**
     * 刷新日志级别
     */
    private void changeLoggingLevel(String key, ConfigChange change) {
        if (null == loggingSystem) {
            return;
        }
        String newLevel = change.getNewValue();
        LogLevel level = LogLevel.valueOf(newLevel.toUpperCase());
        loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
        LOGGER.info("【Apollo-logger-config-change】>> {} -> {}", key, newLevel);
    }
}