package com.scheduled.dynamic.task;

import com.scheduled.dynamic.service.ScheduledTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务 02
 *
 * @author 码农猿
 */
public class ScheduledTask02 implements ScheduledTaskJob {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask02.class);

    @Override
    public void run() {
        LOGGER.info("ScheduledTask => 02  run  当前线程名称 {} ", Thread.currentThread().getName());
    }
}