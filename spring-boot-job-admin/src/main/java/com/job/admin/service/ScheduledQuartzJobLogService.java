package com.job.admin.service;

import com.job.admin.web.param.bo.JobTaskLogDetailBO;
import com.job.admin.web.param.bo.JobTaskLogPageQueryBO;
import com.job.admin.web.param.vo.ScheduledQuartzJobLogDetailVO;
import com.job.admin.web.param.vo.ScheduledQuartzJobLogPageVO;

/**
 * 任务日志 service
 *
 * @author mengq
 * @version 1.0
 * @date 2020-04-12 14:38
 */
public interface ScheduledQuartzJobLogService {

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
