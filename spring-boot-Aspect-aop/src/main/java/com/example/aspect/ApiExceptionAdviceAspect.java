package com.example.aspect;

import com.example.annotation.ApiAnnotation;
import com.example.bean.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Facade 接口统一异常处理切面
 *
 * @author 码农猿
 */
@Slf4j
@Aspect
@Component
public class ApiExceptionAdviceAspect {

    /**
     * 匹配所有使用以下注解的方法
     *
     * @see ApiAnnotation
     */
    @Pointcut("@annotation(com.example.annotation.ApiAnnotation)")
    public void pointCut() {
    }


    @Around("pointCut()&&@annotation(adviceLog)")
    public Object logAround(ProceedingJoinPoint joinPoint, ApiAnnotation adviceLog) {
        //方法返回结果
        Object result = null;
        try {
            log.info("【Api-切面处理】>> {} >> 处理开始 ", adviceLog.description());
            //执行方法
            result = joinPoint.proceed();
            log.info("【Api-切面处理】>> {} >> 处理结束 ", adviceLog.description());
            return result;
        } catch (Throwable e) {
            Response apiResult = new Response();
            //其它未知异常捕获
            log.error("【Api-切面处理】>> {} >> 发生异常 , stack = {}", adviceLog.description(), ExceptionUtils.getStackTrace(e));

            String errorMsg = MessageFormat.format("{0} >> 发生异常了", adviceLog.description());
            apiResult.setSuccess(false);
            apiResult.setErrorMsg(errorMsg);
            return apiResult;
        }
    }

}