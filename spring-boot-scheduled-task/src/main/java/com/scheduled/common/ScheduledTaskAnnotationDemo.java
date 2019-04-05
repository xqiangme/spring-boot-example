package com.scheduled.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 普通注解式定时任务 demo
 * 缺点：不利于管理，更改表达式需要重启服务
 *
 * @author 码农猿
 */
@Component
public class ScheduledTaskAnnotationDemo {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskAnnotationDemo.class);

    //    @Scheduled(cron = "${task.corn}")
    public void scheduledTask01() {
        LOGGER.info("注解式启动 task 01  当前线程 {} ", Thread.currentThread().getName());
        //此处调用需要执行的任务代码
    }

    //    @Scheduled(cron = "0/3 * * * * ?")
    public void scheduledTask02() {
        LOGGER.info("注解式启动 task 02  当前线程 {} ", Thread.currentThread().getName());
        //此处调用需要执行的任务代码
    }
}