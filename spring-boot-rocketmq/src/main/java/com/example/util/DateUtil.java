package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化
 *
 * @author 程序员小强
 */
public class DateUtil {

    /**
     * 时间格式化
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间格式化
     */
    public static String formatDateMillisecond(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 时间格式化
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}