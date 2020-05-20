package com.job.admin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户类型枚举
 *
 * @author mengq
 * @date 2020-04-17 17:46
 */
public enum ScheduledUserTypeEnum {

    /**
     * 超级管理员
     */
    SUPPER_ADMIN(1, 3),

    /**
     * 普通管理员
     */
    ADMIN(2, 2),

    /**
     * 普通用户
     */
    USER(3, 1),

    ;

    private Integer type;

    /**
     * 等级 越小越高
     */
    private Integer level;

    ScheduledUserTypeEnum(Integer type, Integer level) {
        this.type = type;
        this.level = level;
    }

    public static Boolean isAdmin(Integer type) {
        if (SUPPER_ADMIN.getType().equals(type)) {
            return true;
        }
        return ADMIN.getType().equals(type);
    }

    public static Integer getLevelByType(Integer type) {
        return getEnumByType(type).getLevel();
    }

    public static ScheduledUserTypeEnum getEnumByType(Integer type) {
        for (ScheduledUserTypeEnum typeEnum : ScheduledUserTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return USER;
    }

    public static Map<Integer, ScheduledUserTypeEnum> getAllType() {
        Map<Integer, ScheduledUserTypeEnum> enumMap = new HashMap<>(ScheduledUserTypeEnum.values().length);
        for (ScheduledUserTypeEnum typeEnum : ScheduledUserTypeEnum.values()) {
            enumMap.put(typeEnum.getType(), typeEnum);
        }
        return enumMap;
    }

    public Integer getType() {
        return type;
    }

    public Integer getLevel() {
        return level;
    }
}
