package com.job.admin.web.param.bo;


import lombok.Data;

@Data
public class JobTaskUpdateBO extends JobTaskSaveBO {

    /**
     * 任务Id
     */
    private Integer id;

}
