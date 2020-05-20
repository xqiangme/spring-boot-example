package com.job.task.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * job-admin 任务监控
 * 可以设置每分钟执行，判断项目是否还活着
 */
@Slf4j
@Component
public class TaskAdminMonitor {

    public void monitor() {
        log.info("[ job-admin-monitor ]> job-admin is alive");
    }

}