package com.job.admin.web.param.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class HomeResultVO implements Serializable {

    private static final long serialVersionUID = 8763039540705743762L;

    /**
     * 当前时间
     */
    private String currentTime;

    /**
     * 任务总数
     */
    private Integer jobTotal;

    /**
     * 启用的任务总数
     */
    private Integer enableJobTotal;

    /**
     * 已停止总数
     */
    private Integer stopJobTotal;

    /**
     * 启动端口
     */
    private String port;

    /**
     * 虚拟机的启动时间
     */
    private String startTime;

    /**
     * 操作系统
     */
    private String systemName;

    /**
     * 操作用户
     */
    private String systemUser;

    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * java版本
     */
    private String javaVersion;

    /**
     * 活动线程的当前数目
     * 包括守护线程和非守护线程
     */
    private Integer activeThreadCount;

    /**
     * 活动线程数
     */
    private Integer peakThreadCount;

    /**
     * 活动守护线程数
     */
    private Integer daemonThreadCount;

    /**
     * 指定的总内存
     */
    private String maxMemory;

    /**
     * 当前分配的可用内存是为新对象准备的当前分配空间,
     * 警告这不是可用的总可用内存：
     */
    private String freeMemory;

    /**
     * 总分配内存
     * 为java进程保留的总分配空间：
     */
    private String totalMemory;

    /**
     * 已用内存
     */
    private String usedMemory;

}
