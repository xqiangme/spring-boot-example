package com.example.common;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

/**
 * 公共-地址常量
 *
 * @author 程序员小强
 */
public class ExcelExportConstant {

    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer MAX_ERROR = 512;
    /**
     * 任务缓存，秒数
     */
    public static final long TASK_CACHE_EXPIRED = 300;

    /**
     * 任务缓存key 前缀
     */
    private static final String TASK_CACHE_KEY_PREFIX = "gen.excel.task.";

    /**
     * 系统用户目录路径
     */
    public final static String SYSTEM_USER_HOME_PATH = System.getProperties().getProperty("user.home");

    /**
     * 临时文件目录
     */
    public static String getTempTaskFilePath(Integer taskId) {
        return SYSTEM_USER_HOME_PATH + "/files/temp/gen-excel/" + taskId;
    }

    public static String getTempExcelFilePath(Integer taskId) {
        return getTempTaskFilePath(taskId) + "/excel";
    }

    public static String getTempExcelFullFileName(String tempExcelFilePath, String fileName) {
        return tempExcelFilePath + "/" + fileName;
    }


    public static String getTempExcelFullFileName(String tempExcelFilePath, String fileName, int page) {
        return tempExcelFilePath + "/" + fileName + "-" + page + ".xlsx";
    }


    public static String getTempZipFullFileName(String tempTaskFilePath, String fileName) {
        return tempTaskFilePath + "/" + fileName + ".zip";
    }

    /**
     * 云存储excel文件名
     */
    public static String getCloudExcelFileName(String fileName, String ossDir) {
        String dir = "";
        if (!ossDir.endsWith("/")) {
            dir = "/";
        }
        return String.format("%s%s%s-%s%s.xlsx", ossDir, dir, fileName,
                DateUtil.format(new Date(), "yyyyMMddHHmmss"), RandomUtil.randomNumbers(4));
    }

    /**
     * 云存储excel压缩包文件名
     */
    public static String getCloudZipFileName(String fileName, String ossDir) {
        String dir = "";
        if (!ossDir.endsWith("/")) {
            dir = "/";
        }
        return String.format("%s%s%s-%s%s.zip", ossDir, dir, fileName,
                DateUtil.format(new Date(), "yyyyMMddHHmmss"), RandomUtil.randomNumbers(4));
    }

    public static String getTaskCacheKey(Integer taskId) {
        return TASK_CACHE_KEY_PREFIX + taskId;
    }

}
