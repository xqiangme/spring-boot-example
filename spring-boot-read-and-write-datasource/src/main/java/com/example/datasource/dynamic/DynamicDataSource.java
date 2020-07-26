package com.example.datasource.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 扩展动态-数据源
 *
 * @author 程序员小强
 * @date 2020-07-26
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    /**
     * 通过路由Key切换数据源
     * <p>
     * spring 在开始进行数据库操作时会通过这个方法来决定使用哪个数据库，
     * 因此我们在这里调用 DynamicDataSourceContextHolder.getDataSourceType()方法获取当前操作类别,
     * 同时可进行读库的负载均衡
     */
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceTypeEnum typeEnum = DynamicDataSourceContextHolder.getDataSourceType();
        logger.info("[ Change data source ] >> " + typeEnum.name());
        return typeEnum;
    }

}