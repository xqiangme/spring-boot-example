package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 公共开关，key值 属性配置
 *
 * @author 码农猿
 */
@Data
@Component
public class ApplicationKeyProperty {

    @Value("${apollo.key.demoKey1}")
    private String demoKey1;



    @Value("${apollo.key.demoKey2}")
    private String demoKey2;

}