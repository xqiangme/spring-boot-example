package com.example.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.config.constant.DataSourceConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源2 >  配置
 *
 * @author 码农猿
 * @date 2019-03-25
 * 说明一下两个注解的作用
 * -- @Primary：在众多相同的bean中，优先选择用@Primary注解的bean（该注解加在各个bean上）
 * -- @Qualifier：在众多相同的bean中，@Qualifier指定需要注入的bean（该注解跟随在@Autowired后）
 */
@Configuration
@MapperScan(basePackages = DataSourceConstant.DB2_BASE_PACKAGES, sqlSessionTemplateRef = "test2SqlSessionTemplate")
public class DataSource2Config {

    /**
     * 数据源配置
     * 使用的连接池是 DruidDataSource
     * <p>
     * 注解ConfigurationProperties
     * 作用就是将全局配置文件中的属性值注入到DruidDataSource 的同名参数
     */
    @Bean(name = "test2DataSource")
    @ConfigurationProperties(prefix = DataSourceConstant.DB2_DATA_SOURCE_PREFIX)
    public DataSource testDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }


    /**
     * 创建 SqlSessionFactory 工厂
     */
    @Bean(name = "test2SqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("test2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //数据源
        bean.setDataSource(dataSource);
        //mapper 地址
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConstant.DB2_MAPPER_LOCATION));
        return bean.getObject();
    }

    /**
     * 事务管理
     */
    @Bean(name = "test2TransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("test2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * MyBatis提供的持久层访问模板化的工具
     * 线程安全，可通过构造参数或依赖注入SqlSessionFactory实例。
     */
    @Bean(name = "test2SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
