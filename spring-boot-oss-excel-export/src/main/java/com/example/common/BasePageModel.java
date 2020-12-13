package com.example.common;

import lombok.Data;

/**
 * 公共导出参数基类
 *
 * @author 程序员小强
 * @date 2020-11-05 21:35
 */
@Data
public class BasePageModel {

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 分页容量
     */
    private Integer pageSize;

    /**
     * 操作人ID
     */
    private String operateBy;

    /**
     * 操作人名称
     */
    private String operateName;
}
