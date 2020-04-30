/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.admin.service;

import com.example.admin.web.param.bo.JobTaskLogDetailBO;
import com.example.admin.web.param.bo.JobTaskLogPageQueryBO;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogDetailVO;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogPageVO;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-12 14:38
 * @desc
 */
public interface TaskJobLogService {

    /**
     * 分页日志列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    ScheduledQuartzJobLogPageVO listPageLog(JobTaskLogPageQueryBO queryBO);

    /**
     * 日志详情
     *
     * @param detailBO
     * @return 日志详情
     * @author mengq
     */
    ScheduledQuartzJobLogDetailVO getLogDetail(JobTaskLogDetailBO detailBO);

}
