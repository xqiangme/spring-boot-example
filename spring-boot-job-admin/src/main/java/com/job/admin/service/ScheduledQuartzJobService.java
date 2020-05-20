package com.job.admin.service;

import com.job.admin.web.param.bo.JobTaskOperateBO;
import com.job.admin.web.param.bo.JobTaskPageQueryBO;
import com.job.admin.web.param.bo.JobTaskSaveBO;
import com.job.admin.web.param.bo.JobTaskUpdateBO;
import com.job.admin.web.param.vo.HomeResultVO;
import com.job.admin.web.param.vo.ScheduledQuartzJobDetailVO;
import com.job.admin.web.param.vo.ScheduledQuartzJobPageVO;

/**
 * 任务管理接口
 *
 * @author mengq
 * @version 1.0
 * @date 2020-04-12 14:38
 */
public interface ScheduledQuartzJobService {

    /**
     * 主页统计与系统信息
     *
     * @return 主页统计与系统信息
     * @author mengq
     */
    HomeResultVO getHomeCount();

    /**
     * 分页任务列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    ScheduledQuartzJobPageVO listPageJob(JobTaskPageQueryBO queryBO);

    /**
     * 任务详情
     *
     * @param operateBO
     * @return 任务详情
     * @author mengq
     */
    ScheduledQuartzJobDetailVO getJobDetail(JobTaskOperateBO operateBO);

    /**
     * 新增任务
     *
     * @param jobSaveBO
     * @author mengq
     */
    void addJob(JobTaskSaveBO jobSaveBO);


    /**
     * 启动任务
     *
     * @param operateBO
     * @author mengq
     */
    void startJob(JobTaskOperateBO operateBO);


    /**
     * 停止任务
     *
     * @param operateBO
     * @author mengq
     */
    void stopJob(JobTaskOperateBO operateBO);


    /**
     * 修改任务
     *
     * @param updateBO
     */
    void updateJob(JobTaskUpdateBO updateBO);

    /**
     * 删除任务
     *
     * @param operateBO
     */
    void deleteJob(JobTaskOperateBO operateBO);

}
