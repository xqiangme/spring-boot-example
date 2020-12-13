package com.example.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oss 配置类
 *
 * @author 程序员小强
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperty {

    /**
     * OSS accessid
     */
    private String accessId;

    /**
     * oss secret
     */
    private String accessKey;

    /**
     * oss endpoint
     */
    private String endpoint;

    /**
     * oss bucket
     */
    private String bucket;

    /**
     * 文件存储前缀
     */
    private String exportExcelDir;

    /**
     * 效期天数,默认3
     */
    private Integer genExcelExpireDay = 1;

}
