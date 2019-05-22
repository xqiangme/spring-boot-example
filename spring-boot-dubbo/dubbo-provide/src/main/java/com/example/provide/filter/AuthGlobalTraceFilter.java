package com.example.provide.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.example.api.result.ApiResult;
import com.example.provide.exception.BaseException;
import com.example.provide.exception.SysExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * RPC 接口过滤器
 * <p>
 * 用途：1.处理统一接口层异常
 *
 */
@Slf4j
@Activate(
        group = {"provider", "consumer"},
        order = -9999
)
public class AuthGlobalTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        String apiName = StringPool.EMPTY;
        try {
            apiName = invoker.getUrl().getPath();
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            //拼接 接口地址+方法名
            apiName = apiName.concat(StringPool.DOT).concat(method.getName());
        } catch (NoSuchMethodException e) {
            log.error("【RPC-接口-过滤器】>> 方法: invoke 发生异常, invoker = {}, invocation = {} , stack = {}",
                    invoker, invocation, ExceptionUtils.getStackTrace(e));
        }
        //客户端IP
        String clientIp = RpcContext.getContext().getRemoteHost();
        log.info("【RPC-接口-过滤器】>> 执行开始 >>  apiName : {} , clientIp: {}", apiName, clientIp);
        long startTime = SystemClock.millisClock().now();
        try {
            Result result = invoker.invoke(invocation);
            Throwable exception = result.getException();
            if (!ObjectUtils.isEmpty(exception)) {
                return processException(apiName, clientIp, exception, startTime);
            } else {
                log.error("【RPC-接口-过滤器】>> 执行结束 >>  apiName : {}, clientIp: {}, time : {} ms , result : {}",
                        apiName, clientIp, SystemClock.millisClock().now() - startTime, result.getValue());
                return result;
            }
        } catch (Exception e) {
            log.error("【RPC-接口-过滤器】>> 方法: invoke 发生异常 >>  apiName : {} ,clientIp: {}, time : {} , invocation : {} , stack : {}",
                    apiName, clientIp, SystemClock.millisClock().now() - startTime, invocation, ExceptionUtils.getStackTrace(e));
            return processException(apiName, clientIp, e, startTime);
        }
    }

    /**
     * 异常处理
     */
    private RpcResult processException(String apiName, String clientIp, Throwable exception, long startTime) {
        ApiResult apiResult = new ApiResult();
        String stack = ExceptionUtils.getStackTrace(exception);
        if (exception instanceof BaseException) {
            String errorCode = ((BaseException) exception).getCode();
            if (StringUtils.isBlank(errorCode)) {
                errorCode = SysExceptionEnum.SYSTEM_BUSY.getCode();
            }
            apiResult.fail(errorCode, exception.getMessage());
            log.error("【RPC-接口-过滤器】>> 常规自定义异常 >>  apiName : {}, clientIp: {}, time : {} ms, result : {} ,stack : {}",
                    apiName, clientIp, SystemClock.millisClock().now() - startTime, apiResult, stack);
            return new RpcResult(apiResult);
        } else {
            apiResult.fail(SysExceptionEnum.SYSTEM_BUSY.getCode(), SysExceptionEnum.SYSTEM_BUSY.getMsg());
            log.error("【RPC-接口-过滤器】>> 发生未知异常 >>  apiName : {}, clientIp: {}, time : {} ms, result : {} , stack : {}",
                    apiName, clientIp, SystemClock.millisClock().now() - startTime, apiResult, stack);
            return new RpcResult(apiResult);
        }
    }
}
