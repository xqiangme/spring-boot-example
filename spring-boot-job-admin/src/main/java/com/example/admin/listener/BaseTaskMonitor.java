package com.example.admin.listener;

import com.example.admin.dao.bean.ScheduledQuartzJobInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class BaseTaskMonitor {

    private Log log = LogFactory.getLog(BaseTaskMonitor.class);

    private static final String DEFAULT_PROJECT = "DEFAULT";

    public void baseTaskMonitor(String project) {
        final String monitorStr = "%s TASK IS VIABLE";
        log.error(String.format(monitorStr, project));
    }

    public static ScheduledQuartzJobInfo generateJobForDebug() {
        return generateJobForProject(DEFAULT_PROJECT);
    }

    public static ScheduledQuartzJobInfo generateJobForProject(String project) {
        ScheduledQuartzJobInfo job = new ScheduledQuartzJobInfo();
        job.setId(-1);
        job.setJobClass("baseTaskMonitor");
        job.setJobMethod("baseTaskMonitor");
        job.setJobArguments(project);
        job.setJobGroup("1");
        job.setJobName("baseTaskMonitor");
        job.setJobStatus(ScheduledQuartzJobInfo.JOB_STATUS_ON);
        job.setCronExpression("0 0/1 * * * ?");
        return job;
    }

}
