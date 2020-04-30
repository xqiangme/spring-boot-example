package com.example.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "job.base.config")
public class BasicJobConfig {
    public String start = "on";

    public String project = "common";

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getProject() {
        return project;
    }


    public void setProject(String project) {
        this.project = project;
    }
}
