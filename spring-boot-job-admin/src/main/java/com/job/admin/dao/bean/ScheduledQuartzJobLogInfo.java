package com.job.admin.dao.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务日志记录表
 *
 * @author mengq
 */
@Data
public class ScheduledQuartzJobLogInfo implements Serializable {

    private static final long serialVersionUID = 8780919295890565640L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 日志内容
     */
    private String projectKey;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 日志描述
     */
    private String logDesc;

    /**
     * 备注
     */
    private String remarks;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 操作人ID
     */
    private String operateId;

    /**
     * 操作人名称
     */
    private String operateName;

    /**
     * 任务ID
     */
    private Integer jobId;

    /**
     * 日志类型 1-新增 2-修改 3-启动 4-关闭 5-删除
     */
    private Integer logType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
