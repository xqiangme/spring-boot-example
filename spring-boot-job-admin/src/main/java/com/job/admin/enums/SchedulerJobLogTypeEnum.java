package com.job.admin.enums;

/**
 * 日志类型枚举
 */
public enum SchedulerJobLogTypeEnum {

    /**
     * 日志类型 1-新增 2-修改 3-启动 4-关闭 5-删除
     */
    CREATE(1, "新增"),
    UPDATE(2, "修改"),
    OPEN(3, "启动"),
    CLOSE(4, "停止"),
    DELETE(5, "删除"),
    ;

    private Integer type;
    private String desc;

    SchedulerJobLogTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
