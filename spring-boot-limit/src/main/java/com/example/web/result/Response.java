package com.example.web.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @author 程序员小强
 */
@Data
public class Response implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final String EMPTY = "";

    /**
     * 请求是否成功
     */
    private Boolean success;
    /**
     * 返回的的数据
     */
    private Object data;
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

    public Response(Boolean success, Object data, String errorCode, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static Response success() {
        return new Response(true, EMPTY, EMPTY, EMPTY);
    }
    public static Response success(Object data) {
        return new Response(true, data, EMPTY, EMPTY);
    }

    public static Response error(String errorCode, String errorMsg) {
        return new Response(false, null, errorCode, errorMsg);
    }
}
