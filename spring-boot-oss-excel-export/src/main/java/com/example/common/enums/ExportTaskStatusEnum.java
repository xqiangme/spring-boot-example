package com.example.common.enums;

/**
 * 导出任务状态枚举
 *
 * @author 程序员小强
 */
public enum ExportTaskStatusEnum {

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    IN_EXECUTE(1, "执行中"),
    SUCCESS(2, "执行成功"),
    FAIL(3, "执行失败"),

    ;

    private int value;
    private String name;

    ExportTaskStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
