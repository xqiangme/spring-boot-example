package com.job.admin.enums;

/**
 * @author mengq
 */
public enum ScheduledUserStatusEnum {

    /**
     * 默认值
     */
    DEFAULT(-1, ""),

    /**
     * 状态 启用
     */
    ENABLE(1, "启用"),

    /**
     * 停用
     */
    DISABLE(2, "停用"),

    /**
     * 锁定
     */
    LOCK(3, "锁定");


    private Integer value;
    private String name;

    ScheduledUserStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;


    }

    public static ScheduledUserStatusEnum getByValue(Integer value) {
        for (ScheduledUserStatusEnum statusEnum : ScheduledUserStatusEnum.values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return ScheduledUserStatusEnum.DEFAULT;
    }

}
