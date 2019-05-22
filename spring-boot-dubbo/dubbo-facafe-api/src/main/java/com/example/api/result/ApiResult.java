package com.example.api.result;

import lombok.Data;

import java.io.Serializable;


@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -2547026529446900321L;

    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 返回的的数据
     */
    private T data;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    public ApiResult() {
    }

    /**
     * 成功请求
     * success : true
     * errorCode : 默认 2000
     * errorMsg : 默认 ""
     */
    public static ApiResult success() {
        return new ApiResult<>(true, "操作成功", "2000", "");
    }

    /**
     * 成功请求
     * success : true
     * errorCode : 默认 2000
     * errorMsg : 默认 ""
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, "2000", "");
    }

    public static <T> ApiResult<T> error(String errorCode, String errorMsg) {
        return new ApiResult<>(false, null, errorCode, errorMsg);
    }

    public ApiResult<T> data(T data) {
        this.success = true;
        this.data = data;
        this.errorCode = "2000";
        this.errorMsg = "";
        return this;
    }

    public ApiResult fail(String errorCode, String errorMsg) {
        this.success = false;
        this.data = null;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        return this;
    }

    public ApiResult(boolean success, T data, String errorCode, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


}
