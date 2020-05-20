package com.job.admin.web.param.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduledQuartzJobPageVO implements Serializable {

    private static final long serialVersionUID = 8763039540705743762L;

    private Integer total;

    private List<ScheduledQuartzJobDetailVO> list;

    public static ScheduledQuartzJobPageVO initDefault() {
        ScheduledQuartzJobPageVO result = new ScheduledQuartzJobPageVO();
        result.setTotal(0);
        result.setList(new ArrayList<>(0));
        return result;
    }

    public ScheduledQuartzJobPageVO() {
    }

    public ScheduledQuartzJobPageVO(Integer total, List<ScheduledQuartzJobDetailVO> list) {
        this.total = total;
        this.list = list;
    }

}
