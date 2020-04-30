package com.example.admin.quartz;

import com.example.config.ApplicationContextHelper;
import com.example.admin.dao.bean.ScheduledQuartzJobInfo;
import com.example.admin.enums.SysExceptionEnum;
import com.example.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.lang.reflect.Method;

/**
 * @author mengqiang
 */
@Slf4j
public class QuartzSchedulerUtil {

    private static Scheduler scheduler;
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    static {
        log.info("[ BaseSchedulerUtil ] >> init");
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            log.error("[ BaseSchedulerUtil ] init exception ", e);
        }
    }

    /**
     * 启动
     */
    public static void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    /**
     * Quartz启动任务
     */
    public static void enable(ScheduledQuartzJobInfo scheduledQuartzJobInfo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduledQuartzJobInfo.getTriggerName(),
                scheduledQuartzJobInfo.getJobGroup());
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //若已经启动-则刷新任务
            if (null != trigger) {
                trigger.getTriggerBuilder().withIdentity(scheduledQuartzJobInfo.getTriggerName(), scheduledQuartzJobInfo.getJobGroup()).withSchedule(
                        CronScheduleBuilder.cronSchedule(scheduledQuartzJobInfo.getCronExpression()))
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("[ BaseSchedulerUtil ] >> enable exist task end triggerName:{},JobGroup:{}", scheduledQuartzJobInfo.getTriggerName(), scheduledQuartzJobInfo.getJobGroup());
                return;
            }

            // 任务执行类
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobBean.class)
                    // 任务名，任务组
                    .withIdentity(scheduledQuartzJobInfo.getJobName(), scheduledQuartzJobInfo.getJobGroup())
                    .build();
            jobDetail.getJobDataMap().put(QuartzJobBean.JOB_ID, scheduledQuartzJobInfo.getId());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_CLASS, scheduledQuartzJobInfo.getJobClass());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_METHOD, scheduledQuartzJobInfo.getJobMethod());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_ARGUMENTS, scheduledQuartzJobInfo.getJobArguments());

            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduledQuartzJobInfo.getTriggerName(), scheduledQuartzJobInfo.getJobGroup()).withSchedule(
                            CronScheduleBuilder.cronSchedule(scheduledQuartzJobInfo.getCronExpression())).build();

            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            throw new BusinessException("任务启动失败");
        }

        log.info("[ BaseSchedulerUtil ] >> enable new task end triggerName:{},JobGroup:{}", scheduledQuartzJobInfo.getTriggerName(), scheduledQuartzJobInfo.getJobGroup());
    }

    /**
     * Quartz停止任务
     */
    public static void disable(ScheduledQuartzJobInfo scheduledQuartzJobInfo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduledQuartzJobInfo.getTriggerName(),
                scheduledQuartzJobInfo.getJobGroup());
        try {

            Trigger trigger = scheduler.getTrigger(triggerKey);

            if (null != trigger) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(new JobKey(scheduledQuartzJobInfo.getJobName(), scheduledQuartzJobInfo.getJobGroup()));
            }

        } catch (Exception e) {
            log.info("[ BaseSchedulerUtil ] >> disable exception triggerName:{},JobGroup:{}", scheduledQuartzJobInfo.getTriggerName(),
                    scheduledQuartzJobInfo.getJobGroup(), e);
            throw new BusinessException("任务停止失败");
        }
        log.info("[ BaseSchedulerUtil ] >> disable end triggerName:{},JobGroup:{}", scheduledQuartzJobInfo.getTriggerName(), scheduledQuartzJobInfo.getJobGroup());
    }

    /**
     * 校验任务类或-方法是否在环境中存在
     */
    public static Boolean checkBeanAndMethodIsExists(String jobClass, String targetMethod, String methodArgs) {
        try {
            checkBeanAndMethodExists(jobClass, targetMethod, methodArgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 校验任务类或-方法是否在环境中存在
     */
    public static void checkBeanAndMethodExists(String jobClass, String targetMethod, String methodArgs) {
        if (null == jobClass) {
            throw new BusinessException(SysExceptionEnum.JOB_CLASS_NOT_EXISTS);
        }
        Object jobClassInfo = ApplicationContextHelper.getContext().getBean(jobClass);
        if (null == jobClassInfo) {
            throw new BusinessException(SysExceptionEnum.JOB_CLASS_NOT_EXISTS, jobClass);
        }
        try {
            //任务参数
            Object[] jobArs = getJobArgs(methodArgs);
            Class jobClazz = jobClassInfo.getClass();
            Class[] parameterType = null;
            if (jobArs != null) {
                parameterType = new Class[jobArs.length];
                for (int i = 0; i < jobArs.length; i++) {
                    parameterType[i] = String.class;
                }
            }
            //执行任务方法
            Method method = jobClazz.getDeclaredMethod(targetMethod, parameterType);
            if (null == method) {
                throw new BusinessException(SysExceptionEnum.JOB_CLASS_METHOD_NOT_EXISTS, jobClass, targetMethod);
            }
        } catch (Exception e) {
            log.error("TaskJobServiceImpl >> checkBeanAndMethodIsExists error ", e);
            throw new BusinessException(SysExceptionEnum.JOB_CLASS_METHOD_NOT_EXISTS, jobClass, targetMethod);
        }
    }

    public static Object[] getJobArgs(String methodArgs) {
        //参数处理
        Object[] args = null;
        if (!StringUtils.isEmpty(methodArgs)) {
            methodArgs = methodArgs + " ";
            String[] argString = methodArgs.split("#&");
            args = new Object[argString.length];
            for (int i = 0; i < argString.length; i++) {
                args[i] = argString[i].trim();
            }
        }
        return args;
    }

}
