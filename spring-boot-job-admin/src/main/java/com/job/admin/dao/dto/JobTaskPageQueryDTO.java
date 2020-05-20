package com.job.admin.dao.dto;

import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务分页查询参数
 *
 * @author mengq
 */
@Builder
@ToString
public class JobTaskPageQueryDTO implements Serializable {

    private static final long serialVersionUID = -4537719275259204794L;

    private Integer limit;

    private Integer pageSize;

    private String projectKey;
    
    private String jobNameLike;

    private String jobMethodLike;

    private Integer jobStatus;

}
