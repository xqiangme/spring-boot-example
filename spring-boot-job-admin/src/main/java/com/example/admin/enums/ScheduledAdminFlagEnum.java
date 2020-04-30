package com.example.admin.enums;

/**
 * @author mengq
 * @date 2020-04-17 17:46
 */
public enum ScheduledAdminFlagEnum {

    /**
     * 管理员标记
     */
    ADMIN_FLAG(1);


    private Integer value;
    private String name;

    ScheduledAdminFlagEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
