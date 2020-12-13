package com.example.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务返回结果
 *
 * @author 程序员小强
 */
@Data
public class ExcelTaskResModel implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 任务备注
     */
    private String remarks;

    /**
     * 下载文件URL
     */
    private String fileUrl;

    /**
     * 任务行数
     */
    private Integer taskRows;

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    private Integer taskStatus;

    /**
     * 执行耗时毫秒数
     */
    private Long taskDual;

}
