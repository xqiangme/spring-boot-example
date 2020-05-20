package com.job.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 任务管理平台配置
 *
 * @author mengq
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "job.base.config")
public class BasicJobConfig {
    public String start = "on";

    /**
     * 项目标识key
     */
    public String projectKey = "common";

    /**
     * 平台名称
     */
    public String platformName = "";

}
