package com.example.bean.result;

import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @author 码农猿
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final String EMPTY = "";
    private static final String SUCCES_CODE = "200";
    private static final String SUCCES_MSG = "操作成功";

    /**
     * 请求是否成功
     */
    private Boolean success;
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

    public Response() {
    }

    /**
     * 成功请求
     * success : true
     * errorCode : 默认 2000
     * errorMsg : 默认 ""
     */
    public static Response success() {
        CommonResult commonResult = new CommonResult(SUCCES_MSG);
        return new Response<>(true, commonResult, SUCCES_CODE, EMPTY);
    }

    /**
     * 成功请求
     * success : true
     * errorCode : 默认 2000
     * errorMsg : 默认 ""
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(true, data, SUCCES_CODE, EMPTY);
    }

    public static <T> Response<T> error(String errorCode, String errorMsg) {
        return new Response<>(false, null, errorCode, errorMsg);
    }

    public Response(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Response(Boolean success, T data, String errorCode, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 操作返回提示语
     */
    static class CommonResult {
        /**
         * 操作返回提示语
         */
        private String message;

        public CommonResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
