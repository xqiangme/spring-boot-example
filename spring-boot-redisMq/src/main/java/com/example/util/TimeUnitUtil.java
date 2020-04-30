package com.example.util;

import java.util.concurrent.TimeUnit;

/**
 * 时间计算工具类
 */
public class TimeUnitUtil {
    /**
     * 时间秒数计算
     *
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return 秒数
     */
    public static int getSeconds(TimeUnit timeUnit, int duration) {
        return (int) timeUnit.toSeconds(duration);
    }
    /**
     * 时间毫秒数计算
     *
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return 毫秒数
     */
    public static long getMillis(TimeUnit timeUnit, int duration) {
        return timeUnit.toMillis(duration);
    }
}