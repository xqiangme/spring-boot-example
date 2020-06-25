package com.example.config.advice;

import com.example.ratelimit.exception.RateLimitException;
import com.example.web.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局统一异常处理
 *
 * @author 程序员小强
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response defaultExceptionHandler(Exception exception) {
        Response result = new Response();
        result.setSuccess(false);

        if (exception instanceof RateLimitException) {
            log.error("[ 全局统一异常处理 ]>>  rateLimit-exception message = {}", exception.getMessage());
            return Response.error("6001", exception.getMessage());
        }

        log.error("[ 全局统一异常处理 ]>>  未知异常 stack = {}", ExceptionUtils.getStackTrace(exception));
        return Response.error("6000", "未知异常");
    }
}