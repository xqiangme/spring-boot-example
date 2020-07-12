package com.example.sso.client.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 创建拦截器
     */
    @Bean
    WebInterceptor webInterceptor() {
        return new WebInterceptor();
    }

    /**
     * 添加拦截器-进行拦截
     * addPathPatterns 添加拦截
     * excludePathPatterns 排除拦截
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.webInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/logOut");
        super.addInterceptors(registry);
    }

    /**
     * 返回值-编码 UTF-8
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    /**
     * 资源处理器-资源路径 映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}