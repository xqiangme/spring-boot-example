package com.example.admin.listener;

import com.example.admin.enums.BaseSchedulerRunEnum;
import com.example.admin.config.BasicJobConfig;
import com.example.admin.dao.mapper.ScheduledQuartzJobMapper;
import com.example.admin.dao.bean.ScheduledQuartzJobInfo;
import com.example.admin.quartz.QuartzSchedulerUtil;
import com.example.admin.quartz.QuartzJobBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-12 14:20
 * @desc
 */
@Slf4j
@Component
public class SchedulerTaskAppRunListener extends ApplicationObjectSupport implements
        ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private BasicJobConfig basicJobConfig;

    @Resource
    private ScheduledQuartzJobMapper scheduledQuartzJobMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {
        //防止重复加载
        if (applicationReadyEvent.getApplicationContext().getParent() != null) {
            return;
        }
        if (basicJobConfig == null || scheduledQuartzJobMapper == null) {
            return;
        }
        log.info("[ SchedulerTaskAppRunListener  ]  project:{}  init task start ", basicJobConfig.getProject());
        ApplicationContext ac = getApplicationContext();
        QuartzJobBean.setAc(ac);
        int enableTaskSize = this.startAll();
        log.info("[ SchedulerTaskAppRunListener  ]  project:{}  init task end enableTaskSize : {}", basicJobConfig.getProject(), enableTaskSize);
    }


    /**
     * 启动全部任务 本地测试的时候，调度器不会启动； 测试环境默认只启动一个Debug用的JOB 线上会按照数据库读取任务
     */
    public int startAll() {
        if (null == basicJobConfig) {
            return 0;
        }

        if (!BaseSchedulerRunEnum.ON.getModel().equals(basicJobConfig.getStart())) {
            return 0;
        }

        try {
            QuartzSchedulerUtil.startScheduler();
            this.addTaskMonitor();
            return this.doRunTaskAfterApplicationRun();
        } catch (Exception e) {
            log.error("[ SchedulerTaskAppRunListener  ] >> project:{} , init task exception ", basicJobConfig.getProject(), e);
        }
        return 0;
    }

    /**
     * 添加心跳监控任务
     */
    private void addTaskMonitor() {
        try {
            // 加入心跳监控任务
            ScheduledQuartzJobInfo monitorJob = BaseTaskMonitor.generateJobForDebug();
            QuartzSchedulerUtil.enable(monitorJob);
        } catch (Exception e) {
            log.error("[ SchedulerTaskAppRunListener  ] >> project:{} , add monitor exception ", basicJobConfig.getProject(), e);
        }
    }

    /**
     * 启动需要启动的任务
     */
    private int doRunTaskAfterApplicationRun() {
        //启用的任务
        int jobStatus = ScheduledQuartzJobInfo.JOB_STATUS_ON;
        //查询任务
        List<ScheduledQuartzJobInfo> list = scheduledQuartzJobMapper.getJobListByProjectAndStatus(basicJobConfig.getProject(), jobStatus);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        log.info("[ SchedulerTaskAppRunListener  ] >> project:{} , need init task size:{} ", basicJobConfig.getProject(), list.size());
        try {
            for (ScheduledQuartzJobInfo job : list) {
                //任务类方法存在校验
                if (!QuartzSchedulerUtil.checkBeanAndMethodIsExists(job.getJobClass(), job.getJobMethod(), job.getJobArguments())) {
                    continue;
                }
                QuartzSchedulerUtil.enable(job);
            }
        } catch (Exception e) {
            log.error("[SchedulerTaskAppRunListener]error when add job. " + e.getMessage(), e);
        }
        return list.size();
    }

}

