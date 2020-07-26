package com.example.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * MyBatis 配置
 *
 * @author 程序员小强
 */
@Configuration
@MapperScan(basePackages = "com.example.mapper")
public class MyBatisConfig {

    @Resource(name = "dynamicDataSource")
    private DataSource dynamicDataSource;

    /**
     * sqlSessionFactory 配置
     * 扫描mybatis下的xml文件
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //指定数据源
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        //mybatis下xml扫描地址
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 事务管理
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
}