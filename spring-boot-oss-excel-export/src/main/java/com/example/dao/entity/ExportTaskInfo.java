package com.example.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * The table 导出任务表
 */
@Data
public class ExportTaskInfo {

    /**
     * 自增ID
     */
    private Integer id;

    private String bizType;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 执行耗时毫秒数
     */
    private Long taskDual;

    /**
     * 任务参数(JSON)
     */
    private String taskParam;

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    private Integer taskStatus;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 效期
     */
    private Date expireTime;

    /**
     * 任务行数
     */
    private Integer taskRows;

    /**
     * 任务备注(失败原因)
     */
    private String remarks;

    private String operateBy;

    private String operateName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
