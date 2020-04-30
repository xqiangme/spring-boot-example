package com.example.admin.web.param.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * The table 任务日志记录表
 */
@Data
public class ScheduledQuartzJobLogItemVO implements Serializable {

    private static final long serialVersionUID = 6621098492149455624L;

    /**
     * 日志ID
     */
    private Integer id;

    /**
     * 任务ID
     */
    private Integer jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务类
     */
    private String jobClass;

    /**
     * 日志类型 1-新增 2-修改 3-启动 4-关闭 5-删除
     */
    private Integer logType;

    /**
     * 日志描述
     */
    private String logDesc;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 操作人ID
     */
    private String operateId;

    /**
     * 操作人名称
     */
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
