package com.job.admin.enums;

/**
 * @author mengq
 */
public enum ScheduledJobStatusEnum {
    /**
     * 默认值
     */
    ON(1, "运行中"),

    /**
     * 停用
     */
    OFF(2, "已停止"),
    ;

    private Integer value;
    private String name;

    ScheduledJobStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
