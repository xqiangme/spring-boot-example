package com.example.advice;

import com.example.enums.SysExceptionEnum;
import com.example.exception.BaseException;
import com.example.model.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局统一异常处理
 *
 * @author 码农猿
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response defaultExceptionHandler(Exception exception) {
        Response result = new Response();
        result.setSuccess(false);
        try {
            throw exception;
        } catch (BaseException e) {
            log.error("【全局异常捕获】>> 自定义业务异常  >> errorMsg : {} ", e.getMsg());
            log.debug("【全局异常捕获】>> 自定义业务异常堆栈  >> stack : {}", ExceptionUtils.getStackTrace(e));
            result.setErrorCode(e.getCode());
            result.setErrorMsg(e.getMessage());
        } catch (MethodArgumentNotValidException argEx) {
            FieldError fieldError = argEx.getBindingResult().getFieldError();
            String errorMsg = String.format("%s >> %s", fieldError.getField(),
                    fieldError.getDefaultMessage());
            log.error("【全局异常捕获】>> 参数校验异常  >>  {}", errorMsg);
            result.setErrorCode(SysExceptionEnum.INVALID_PARAM.getCode());
            result.setErrorMsg(errorMsg);
        } catch (HttpRequestMethodNotSupportedException e) {
            String errorMsg = String.format("请求方式 %s 错误 ! 请使用 %s 方式", e.getMethod(), e.getSupportedHttpMethods());
            log.error("【全局异常捕获】>> {}", errorMsg);
            result.setErrorCode(SysExceptionEnum.INVALID_PARAM.getCode());
            result.setErrorMsg(errorMsg);
        } catch (HttpMediaTypeNotSupportedException e) {
            String errorMsg = String.format("请求类型 %s 错误 ! 请使用 %s 方式", e.getContentType(), e.getSupportedMediaTypes());
            log.error("【全局异常捕获】>> {}", errorMsg);
            result.setErrorCode(SysExceptionEnum.INVALID_PARAM.getCode());
            result.setErrorMsg(errorMsg);
        } catch (Exception e) {
            log.error("【全局异常捕获】>>  未知异常 stack : {}", ExceptionUtils.getStackTrace(e));
            result.setErrorCode(SysExceptionEnum.SYSTEM_ERROR.getCode());
            result.setErrorMsg(SysExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return result;
    }

}