package com.job.admin.enums;

public enum BaseSchedulerRunEnum {

    /**
     * 线上配置
     */
    ON("on"),
    /**
     * 本地配置
     */
    OFF("off"),
    /**
     * 测试环境配置
     */
    DEBUG("debug"),
    ;

    private String model;

    public String getModel() {
        return model;
    }

    private BaseSchedulerRunEnum(String model) {
        this.model = model;
    }
}
