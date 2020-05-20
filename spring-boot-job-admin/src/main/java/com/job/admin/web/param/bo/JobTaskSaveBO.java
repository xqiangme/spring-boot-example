package com.job.admin.web.param.bo;


import lombok.Getter;

/**
 * @author mengq
 */
@Getter
public class JobTaskSaveBO extends JobBaseOperateBO {

    private static final long serialVersionUID = -103737937206837656L;

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
     * 任务时间表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String description;

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass == null ? null : jobClass.trim();
    }

    public void setJobMethod(String jobMethod) {
        this.jobMethod = jobMethod == null ? null : jobMethod.trim();
    }

    public void setJobArguments(String jobArguments) {
        this.jobArguments = jobArguments == null ? null : jobArguments.trim();
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();

    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
