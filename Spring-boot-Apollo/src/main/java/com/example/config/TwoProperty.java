package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公共开关，key值 属性配置
 *
 * @author 码农猿
 */
@Data
@Component
@ConfigurationProperties(prefix = "apollo.demo.two.key")
public class TwoProperty {

    /**
     * 测试数字 1
     */
    private Integer number1;

    /**
     * 测试数字 2
     */
    private Integer number2;


    /**
     * 测试字符串 1
     */
    private String str1;

    /**
     * 测试字符串 2
     */
    private String str2;

}