package com.example.config.constant;

/**
 * 数据源常量
 *
 * @author 码农猿
 * @date 2019-03-25
 */
public class DataSourceConstant {

    //************** 数据源1 配置 **************
    /**
     * mapper 接口包地址
     */
    public static final String DB1_BASE_PACKAGES = "com.example.mapper.db1";
    /**
     * 数据源配置 前缀
     */
    public static final String DB1_DATA_SOURCE_PREFIX = "spring.datasource.test1";
    /**
     * mapper xml文件地址
     */
    public static final String DB1_MAPPER_LOCATION = "classpath:mybatis/mapper/db1/*.xml";


    //************** 数据源 2 配置 **************
    /**
     * mapper 接口包地址
     */
    public static final String DB2_BASE_PACKAGES = "com.example.mapper.db2";
    /**
     * 数据源配置 前缀
     */
    public static final String DB2_DATA_SOURCE_PREFIX = "spring.datasource.test2";
    /**
     * mapper xml文件地址
     */
    public static final String DB2_MAPPER_LOCATION = "classpath:mybatis/mapper/db2/*.xml";
}