package com.example.admin.enums;

import org.apache.commons.lang.StringUtils;

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

    public static BaseSchedulerRunEnum getByModel(String model) {
        if (StringUtils.isBlank(model)) {
            return null;
        }
        BaseSchedulerRunEnum[] objs = BaseSchedulerRunEnum.class.getEnumConstants();
        for (BaseSchedulerRunEnum obj : objs) {
            if (obj.getModel().equalsIgnoreCase(model)) {
                return obj;
            }
        }
        return null;
    }
}
