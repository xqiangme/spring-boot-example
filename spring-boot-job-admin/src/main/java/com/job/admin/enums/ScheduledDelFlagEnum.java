package com.job.admin.enums;

/**
 * @author mengq
 */
public enum ScheduledDelFlagEnum {

    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 删除
     */
    DELETE(1);

    private Integer value;

    ScheduledDelFlagEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
