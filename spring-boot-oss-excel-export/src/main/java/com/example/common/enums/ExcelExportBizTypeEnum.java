package com.example.common.enums;

import lombok.Getter;

/**
 * 导出业务类型枚举
 *
 * @author 程序员小强
 * @date 2020-11-06 22:38
 */
@Getter
public enum ExcelExportBizTypeEnum {

    /**
     * 业务类型枚举
     */
    USER_INFO("user_info", "用户信息", 1, 100),
    STUDENT_INFO("student_info", "学生信息", 1, 100, true),
    ;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 导出-文件前缀名
     * 示例：用户信息+yyyyMMddHHmmss+4位随机数
     */
    private String fileName;

    /**
     * 分页容量
     */
    private Integer pageSize;

    /**
     * 最大循环页数
     * 注：防止死循环，设置一个最大循环次数
     */
    private Integer maxLoopPageSize;

    /**
     * 是否长期
     */
    private boolean neverExpire;

    ExcelExportBizTypeEnum(String bizType, String fileName, Integer pageSize, Integer maxLoopPageSize) {
        this.bizType = bizType;
        this.fileName = fileName;
        this.pageSize = pageSize;
        this.maxLoopPageSize = maxLoopPageSize;
    }

    ExcelExportBizTypeEnum(String bizType, String fileName, Integer pageSize, Integer maxLoopPageSize, boolean neverExpire) {
        this.bizType = bizType;
        this.fileName = fileName;
        this.pageSize = pageSize;
        this.maxLoopPageSize = maxLoopPageSize;
        this.neverExpire = neverExpire;
    }

}
