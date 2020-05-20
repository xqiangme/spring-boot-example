package com.job.admin.web.param.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-14 00:00
 */
@Data
public class JobTaskLogPageQueryBO implements Serializable {


    private static final long serialVersionUID = 8356494977556356252L;
    private Integer page;
    private Integer limit;

    private Integer logType;
    private Integer jobId;
    private String jobNameLike;


    private String operateId;
    private String operateNameLike;
    private String contentLike;

    private String createStartTime;
    private String createEndTime;

}
