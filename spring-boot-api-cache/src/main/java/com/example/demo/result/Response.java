package com.example.demo.result;

import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @author mengq
 */
public class Response implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final String EMPTY = "";
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 编码
     */
    private Integer code;


    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回的的数据
     */
    private Object data;


    public Response() {
    }


    public static Response success() {
        return success(null);
    }

    public static Response success(Object data) {
        return new Response(data, SUCCESS_CODE, EMPTY);
    }

    public static Response error(Integer errorCode, String errorMsg) {
        return new Response(null, errorCode, errorMsg);
    }


    public Response(Object data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
