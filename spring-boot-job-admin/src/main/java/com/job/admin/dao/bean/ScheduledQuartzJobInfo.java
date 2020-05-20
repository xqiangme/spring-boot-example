package com.job.admin.dao.bean;


import com.job.admin.enums.ScheduledJobStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务表
 *
 * @author mengq
 */
@Data
public class ScheduledQuartzJobInfo implements Serializable {

    private static final long serialVersionUID = -1143048553498524804L;


    /**
     * 任务ID
     */
    private Integer id;

    /**
     * 项目
     */
    private String projectKey;

    /**
     * 任务接口类名
     */
    private String jobClass;

    /**
     * 任务接口方法
     */
    private String jobMethod;

    /**
     * 任务参数
     */
    private String jobArguments;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务状态
     *
     * @see ScheduledJobStatusEnum
     */
    private Integer jobStatus;

    /**
     * 任务时间表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最新修改人
     */
    private String updateBy;

    /**
     * 最新修改人名称
     */
    private String updateName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标记
     */
    private Integer delFlag;

    public String getTriggerName() {
        return "ScheduledQuartzJobInfo" + ":" + this.projectKey + ":" + this.getJobName() + ":Trigger";
    }
}
