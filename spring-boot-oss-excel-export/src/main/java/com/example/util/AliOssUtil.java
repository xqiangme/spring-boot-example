package com.example.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * oss 工具
 *
 * @author 程序员小强
 */
public class AliOssUtil {

    private static final String DOT = ".";
    private static final String SLASH = "/";
    private static final String HTTPS_PREFIX = "https://";

    private static Logger logger = LoggerFactory.getLogger(AliOssUtil.class);

    public AliOssUtil() {
    }

    public static OSS getAliOssConfig(String endpoint, String accessKeyId, String accessKeySecret) {
        Assert.notNull(endpoint, "oss连接 > endpoint 不能为空");
        Assert.notNull(endpoint, "oss连接 > accessKeyId 不能为空");
        Assert.notNull(endpoint, "oss连接 > accessKeySecret 不能为空");
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * @param bucketName
     * @param ossFileName oss 文件地址 示例：dev/a.txt
     */
    public static boolean uploadFile(String bucketName, String ossFileName, File file, OSS ossClient) {
        try {
            InputStream input = new FileInputStream(file);
            ossClient.putObject(bucketName, ossFileName, input);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            logger.error("GenExcelOssUtil ---- >> uploadFile 上传异常, e = {}", ExceptionUtil.stacktraceToString(e));
            ossClient.shutdown();
            return false;
        }
    }

    /**
     * @param bucketName
     * @param ossFileName   oss 文件地址 示例：dev/a.txt
     * @param localFilePath 本地文件全路径
     */
    public static boolean uploadFile(String bucketName, String ossFileName, String localFilePath, OSS ossClient) {
        try {
            File file = new File(localFilePath);
            InputStream input = new FileInputStream(file);
            ossClient.putObject(bucketName, ossFileName, input);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            logger.error("GenExcelOssUtil ---- >> uploadFile 上传异常, e = {}", ExceptionUtil.stacktraceToString(e));
            ossClient.shutdown();
            return false;
        }
    }

    /**
     * @param bucketName
     * @param ossFileName   oss 文件地址 示例：dev/a.txt
     * @param localFilePath 本地文件全路径
     */
    public static boolean uploadFile(String bucketName, String ossFileName, String localFilePath, OSS ossClient, ObjectMetadata metadata) {
        try {
            File file = new File(localFilePath);
            InputStream input = new FileInputStream(file);
            ossClient.putObject(bucketName, ossFileName, input, metadata);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            logger.error("GenExcelOssUtil ---- >> uploadFile 上传异常, e = {}", ExceptionUtil.stacktraceToString(e));
            ossClient.shutdown();
            return false;
        }
    }

    public static boolean uploadFileByInputStream(String bucketName, String key, InputStream inputStream, OSS ossClient) {
        try {
            ossClient.putObject(bucketName, key, inputStream);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            logger.error("GenExcelOssUtil ---- >> uploadFile 上传异常, e = {}", ExceptionUtil.stacktraceToString(e));
            ossClient.shutdown();
            return false;
        }
    }

    public static void downloadFile(String bucketName, String ossFileName, String filename, OSS ossClient) {
        try {
            ossClient.getObject(new GetObjectRequest(bucketName, ossFileName), new File(filename));
            ossClient.shutdown();
        } catch (Exception e) {
            logger.error("GenExcelOssUtil ---- >> downloadFile 下载异常, e = {}", ExceptionUtil.getMessage(e));
            ossClient.shutdown();
        }
    }

    /**
     * 拼接文件访问路径 - 永久有效
     */
    public static String getFileUrl(String endpoint, String bucketName, String ossFileName) {
        if (!endpoint.endsWith(SLASH)) {
            endpoint = endpoint + SLASH;
        }
        return HTTPS_PREFIX + bucketName + DOT + endpoint + ossFileName;
    }

    /**
     * 拼接文件访问路径 - 自定义效期
     */
    public static String getExpiredFileUrl(String bucketName, String ossFileName, OSS ossClient, long expiredSecond) {
        String downloadUrl = "";
        try {
            //有效期
            Date expiredTime = new Date(System.currentTimeMillis() + expiredSecond * 1000);
            URL url = ossClient.generatePresignedUrl(bucketName, ossFileName, expiredTime);
            downloadUrl = url.toString();
            ossClient.shutdown();
            return downloadUrl;
        } catch (Exception e) {
            ossClient.shutdown();
            logger.error("GenExcelOssUtil ---- >> fileUrl 下载地址异常, e = {}", ExceptionUtil.getMessage(e));
            return downloadUrl;
        }
    }
}
