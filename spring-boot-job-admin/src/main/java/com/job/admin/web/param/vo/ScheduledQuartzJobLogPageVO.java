package com.job.admin.web.param.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The table 任务日志记录表
 */
@Data
public class ScheduledQuartzJobLogPageVO implements Serializable {

    private Integer total;

    private List<ScheduledQuartzJobLogItemVO> list;

    public static ScheduledQuartzJobLogPageVO initDefault() {
        ScheduledQuartzJobLogPageVO result = new ScheduledQuartzJobLogPageVO();
        result.setTotal(0);
        result.setList(new ArrayList<>(0));
        return result;
    }

    public ScheduledQuartzJobLogPageVO() {
    }

    public ScheduledQuartzJobLogPageVO(Integer total, List<ScheduledQuartzJobLogItemVO> list) {
        this.total = total;
        this.list = list;
    }


}
