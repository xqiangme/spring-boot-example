package com.scheduled.dynamic.task;

import com.scheduled.dynamic.service.ScheduledTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务 05
 *
 * @author 码农猿
 */
public class ScheduledTask05 implements ScheduledTaskJob {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask05.class);

    @Override
    public void run() {
        LOGGER.info("ScheduledTask => 05  run  当前线程名称 {} ", Thread.currentThread().getName());
    }
}