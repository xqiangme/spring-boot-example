package com.example.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * 数据源 配置
 */
@Configuration
@MapperScan(basePackages = "com.example.dal.demo.mapper", sqlSessionTemplateRef = "sessionTemplate")
public class DataSourceConfig {

    /**
     * 数据源配置
     * 使用的连接池是 DruidDataSource
     * <p>
     * 注解ConfigurationProperties
     * 作用就是将全局配置文件中的属性值注入到DruidDataSource 的同名参数
     */
    @Primary
    @Bean(name = "createDataSource")
    @Qualifier("createDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource createDataSource() {
        //DataSourceBuilder.create().build()
        //默认数据源类型是 org.apache.tomcat.jdbc.pool.DataSource
        //这里指定使用类型 -- 阿里DruidDataSource 连接池
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }


    /**
     * 创建 SqlSessionFactory 工厂
     */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory createSqlSessionFactory(@Qualifier("createDataSource") DataSource dataSource, @Qualifier("mysqlPageHelperPlugin") PageInterceptor pageInterceptor) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        String path = "classpath:com/example/dal/demo/mapper/xml/*.xml";
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(path));
        //添加分页插件
        sessionFactoryBean.getObject().getConfiguration().addInterceptor(pageInterceptor);
        return sessionFactoryBean.getObject();
    }


    /**
     * 配置 分页插件 pageHelper(使用该类是放开注释)
     */
    @Primary
    @Bean(name = "mysqlPageHelperPlugin")
    public PageInterceptor buildPageHelperPlugin() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "false");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    /**
     * 事务管理
     */
    @Primary
    @Bean(name = "test1TransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("createDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * MyBatis提供的持久层访问模板化的工具
     * 线程安全，可通过构造参数或依赖注入SqlSessionFactory实例
     */
    @Primary
    @Bean(name = "sessionTemplate")
    public SqlSessionTemplate createSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
