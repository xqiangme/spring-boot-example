package com.job.admin.dao.dto;

import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务日志查询参数
 *
 * @author mengq
 */
@Builder
@ToString
public class JobTaskLogPageQueryDTO implements Serializable {

    private static final long serialVersionUID = -2794336177217543563L;

    private Integer limit;
    private Integer pageSize;

    private String projectKey;
    private Integer logType;
    private Integer jobId;
    private String jobNameLike;

    private String operateId;
    private String operateNameLike;
    private String contentLike;
    private Date createStartTime;
    private Date createEndTime;

}
