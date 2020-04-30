package com.example.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.admin.config.BasicJobConfig;
import com.example.admin.dao.mapper.ScheduledQuartzJobLogMapper;
import com.example.admin.dao.bean.ScheduledQuartzJobLogInfo;
import com.example.admin.web.param.bo.JobTaskLogDetailBO;
import com.example.admin.web.param.bo.JobTaskLogPageQueryBO;
import com.example.admin.dao.dto.JobTaskLogPageQueryDTO;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogDetailVO;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogItemVO;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogPageVO;
import com.example.admin.service.TaskJobLogService;
import com.example.admin.util.BeanCopierUtil;
import com.example.admin.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-16 23:06
 * @desc
 */
@Slf4j
@Service
public class TaskJobLogServiceImpl implements TaskJobLogService {

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
        String jobProject = basicJobConfig.getProject();
        if (StringUtils.isBlank(jobProject)) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }
        //构建查询参数
        JobTaskLogPageQueryDTO pageQueryDTO = this.buildLogQueryParam(queryBO);
        Integer total = scheduledQuartzJobLogMapper.countByCondition(pageQueryDTO);
        if (total <= 0) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }

        List<ScheduledQuartzJobLogItemVO> logList = scheduledQuartzJobLogMapper.listPageByCondition(pageQueryDTO);
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
        ScheduledQuartzJobLogDetailVO result = BeanCopierUtil.copy(logInfo, ScheduledQuartzJobLogDetailVO.class);
        result.setCreateTimeStr(DateUtil.formatDateTime(logInfo.getCreateTime()));
        return result;
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
                .projectKey(basicJobConfig.getProject())
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
