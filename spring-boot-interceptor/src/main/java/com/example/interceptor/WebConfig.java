package com.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 创建拦截器
     */
    @Bean
    WebInterceptor webInterceptor() {
        log.info("创建 WebConfig >> webInterceptor 。。。 line 30 ");
        return new WebInterceptor();
    }

    /**
     * 添加拦截器-进行拦截
     * addPathPatterns 添加拦截
     * excludePathPatterns 排除拦截
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("创建 WebConfig >> addInterceptors 。。。 line 43 ");
        registry.addInterceptor(this.webInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/in");
        super.addInterceptors(registry);
    }

    /**
     * 添加自定义注解方式参数处理
     * 示例：登录用户信息
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        log.info("创建 WebConfig >> addArgumentResolvers 。。。 line 31 ");
        argumentResolvers.add(new CurrentUserMethodArgumentHandler());
        log.info("创建 WebConfig >> addArgumentResolvers 。。。 line 53 ");
    }


    /**
     * 返回值-编码 UTF-8
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        log.info("创建 WebConfig >> responseBodyConverter 。。。 line 56 ");
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }


    /**
     * 配置内容裁决的一些选项
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        log.info("创建 WebConfig >> configureContentNegotiation 。。。 line 62 ");
        configurer.favorPathExtension(false);
    }

    /**
     * 资源处理器
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}