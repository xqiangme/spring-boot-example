package com.scheduled.dynamic.task;

import com.scheduled.dynamic.service.ScheduledTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务 04
 *
 * @author 码农猿
 */
public class ScheduledTask04 implements ScheduledTaskJob {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask04.class);

    @Override
    public void run() {
        LOGGER.info("ScheduledTask => 04  run  当前线程名称 {} ", Thread.currentThread().getName());
    }
}