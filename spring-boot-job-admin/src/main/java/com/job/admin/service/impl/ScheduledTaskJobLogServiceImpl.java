package com.job.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.job.admin.config.BasicJobConfig;
import com.job.admin.dao.bean.ScheduledQuartzJobLogInfo;
import com.job.admin.dao.dto.JobTaskLogPageQueryDTO;
import com.job.admin.dao.mapper.ScheduledQuartzJobLogMapper;
import com.job.admin.service.ScheduledQuartzJobLogService;
import com.job.admin.util.BeanCopierUtil;
import com.job.admin.util.PageUtils;
import com.job.admin.web.param.bo.JobTaskLogDetailBO;
import com.job.admin.web.param.bo.JobTaskLogPageQueryBO;
import com.job.admin.web.param.vo.ScheduledQuartzJobLogDetailVO;
import com.job.admin.web.param.vo.ScheduledQuartzJobLogItemVO;
import com.job.admin.web.param.vo.ScheduledQuartzJobLogPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务日志 service
 *
 * @author mengq
 * @version 1.0
 * @date 2020-04-12 14:38
 */
@Slf4j
@Service
public class ScheduledTaskJobLogServiceImpl implements ScheduledQuartzJobLogService {

    @Resource
    private BasicJobConfig basicJobConfig;

    @Resource
    private ScheduledQuartzJobLogMapper scheduledQuartzJobLogMapper;

    /**
     * 分页日志列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    @Override
    public ScheduledQuartzJobLogPageVO listPageLog(JobTaskLogPageQueryBO queryBO) {
        String projectKey = basicJobConfig.getProjectKey();
        if (StringUtils.isBlank(projectKey)) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }
        //构建查询参数
        JobTaskLogPageQueryDTO pageQueryDTO = this.buildLogQueryParam(queryBO);
        Integer total = scheduledQuartzJobLogMapper.countByCondition(pageQueryDTO);
        if (total <= 0) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }

        List<ScheduledQuartzJobLogItemVO> logList = scheduledQuartzJobLogMapper.listPageByCondition(pageQueryDTO);
        if (CollectionUtils.isEmpty(logList)) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }
        return new ScheduledQuartzJobLogPageVO(total, logList);
    }


    /**
     * 日志详情
     *
     * @param detailBO
     * @return 日志详情
     * @author mengq
     */
    @Override
    public ScheduledQuartzJobLogDetailVO getLogDetail(JobTaskLogDetailBO detailBO) {
        ScheduledQuartzJobLogInfo logInfo = scheduledQuartzJobLogMapper.getById(detailBO.getId());
        return BeanCopierUtil.copy(logInfo, ScheduledQuartzJobLogDetailVO.class);
    }

    /**
     * 构建查询参数
     *
     * @param queryBO
     * @return
     */
    private JobTaskLogPageQueryDTO buildLogQueryParam(JobTaskLogPageQueryBO queryBO) {
        //构建查询参数
        return JobTaskLogPageQueryDTO.builder()
                .projectKey(basicJobConfig.getProjectKey())
                .limit(PageUtils.getStartRow(queryBO.getPage(), queryBO.getLimit()))
                .pageSize(PageUtils.getOffset(queryBO.getLimit()))
                .logType(queryBO.getLogType())
                .jobId(queryBO.getJobId())
                .jobNameLike(queryBO.getJobNameLike())
                .operateId(queryBO.getOperateId())
                .operateNameLike(queryBO.getOperateNameLike())
                .contentLike(queryBO.getContentLike())
                .createStartTime(StringUtils.isBlank(queryBO.getCreateStartTime()) ?
                        null : DateUtil.parseDateTime(queryBO.getCreateStartTime()))
                .createEndTime(StringUtils.isBlank(queryBO.getCreateEndTime()) ?
                        null : DateUtil.parseDateTime(queryBO.getCreateEndTime()))
                .build();
    }
}
