package com.example.interceptor;

import com.example.anno.CurrentUser;
import com.example.model.UserLoginModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * 方法参数拦截器
 */
@Slf4j
public class CurrentUserMethodArgumentHandler implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("[参数处理] >> CurrentUserMethodArgumentHandler >> supportsParameter 。。。 line 20 ");
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("[参数处理] >> CurrentUserMethodArgumentHandler >> resolveArgument 。。。 line 26 ");
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setUserId("100001");
        userLoginModel.setUserName("测试人员");
        return userLoginModel;
    }
}
