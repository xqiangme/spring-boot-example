package com.example.comment;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import com.example.comment.redisson.RedisCache;
import com.example.common.BasePageModel;
import com.example.common.ExcelExportConstant;
import com.example.common.ExcelTaskResModel;
import com.example.common.enums.ExcelExportBizTypeEnum;
import com.example.common.enums.ExportTaskStatusEnum;
import com.example.config.property.OssProperty;
import com.example.dao.entity.ExportTaskInfo;
import com.example.dao.mapper.ExportTaskInfoMapper;
import com.example.util.AliOssUtil;
import com.example.util.BeanCopierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用导分页导出组件
 *
 * @author 程序员小强
 */
@Slf4j
@Component
public class ExcelExportComment {

    @Resource
    private RedisCache redisCache;
    @Resource
    private OssProperty ossProperty;
    @Resource
    private ExportTaskInfoMapper exportTaskInfoMapper;

    /**
     * 执行导出
     */
    public Integer invoke(ExcelExportInvoker invoker, BasePageModel param, ExcelExportBizTypeEnum exportEnum, Class<?> tClass) {
        //创建任务
        Integer taskId = this.doAddTask(exportEnum, param);
        this.export(taskId, invoker, param, exportEnum, tClass);
        return taskId;
    }

    /**
     * 异步分页-导出方法
     */
    @Async
    public void export(Integer taskId, ExcelExportInvoker invoker, BasePageModel param, ExcelExportBizTypeEnum exportEnum, Class<?> tClass) {
        List<?> exportList = null;
        int page = param.getPage();
        if (null == param.getPageSize()) {
            param.setPageSize(exportEnum.getPageSize());
        }
        String fileName = exportEnum.getFileName();
        long start = System.currentTimeMillis();
        //任务临时文件目录
        String tempTaskFilePath = ExcelExportConstant.getTempTaskFilePath(taskId);
        //excel临时文件目录
        String tempExcelFilePath = ExcelExportConstant.getTempExcelFilePath(taskId);
        if (!FileUtil.exist(tempExcelFilePath)) {
            FileUtil.mkdir(tempExcelFilePath);
        }
        log.info("[ 公共导出组件 ] 执行开始 >> taskId:{},fileName:{}", taskId, fileName);
        int totalRows = 0;
        try {
            while (true) {
                //最大循环次数保护-反正死循环
                if (page > exportEnum.getMaxLoopPageSize()) {
                    log.error("[ 公共导出组件 ] 超过最大循环次数 >> taskId:{},fileName:{},page:{}", taskId, fileName, page);
                    throw new RuntimeException("超过最大循环次数");
                }
                exportList = invoker.invoke(param);
                if (CollectionUtils.isEmpty(exportList)) {
                    break;
                }
                //累计总行数
                totalRows = totalRows + exportList.size();
                //生成excel临时文件全路径名
                String tempExcelFullFileName = ExcelExportConstant.getTempExcelFullFileName(tempExcelFilePath, fileName, page);
                //写出excel临时文件
                EasyExcel.write(tempExcelFullFileName, tClass).sheet(fileName).doWrite(exportList);
                page++;
                param.setPage(page);
            }
            //上传到OSS
            String fileUrl = this.doUploadOss(tempTaskFilePath, tempExcelFilePath, fileName, exportEnum.isNeverExpire());
            long time = (System.currentTimeMillis() - start);
            //执行成功-更新任务表
            this.updateSuccess(time, taskId, fileUrl, totalRows);
            log.info("[ 公共导出组件 ] 执行结束 >> taskId:{},fileName:{}time:{},page:{},totalRows:{}",
                    taskId, fileName, time, page, totalRows);
        } catch (Exception e) {
            log.error("[ 公共导出组件 ] >> 导出文件失败 ", e);
            //执行失败-更新任务表
            this.updateFail((System.currentTimeMillis() - start), taskId, e.getMessage());
            throw new RuntimeException("导出文件失败");
        } finally {
            //刷新任务缓存
            this.refreshTaskCache(taskId);
            //无论是否出现异常均清除任务临时文件
            if (FileUtil.exist(tempTaskFilePath)) {
                FileUtil.del(tempTaskFilePath);
            }
        }
    }

    /**
     * 查询任务
     *
     * @param taskId
     */
    public ExcelTaskResModel getTaskById(Integer taskId) {
        String cacheKey = ExcelExportConstant.getTaskCacheKey(taskId);
        String task = redisCache.getString(cacheKey);
        //缓存存在-直接返回
        if (!StringUtils.isEmpty(task)) {
            return JSON.parseObject(task, ExcelTaskResModel.class);
        }
        log.info("[{}] >> [公共导出] >> 缓存不存在 > 查询DB获取任务结果", taskId);
        //这里为了防止缓存穿透-可以加锁

        //根任务ID查询
        ExportTaskInfo taskInfo = exportTaskInfoMapper.getById(taskId);
        if (null == taskInfo) {
            throw new RuntimeException("任务不存在");
        }
        ExcelTaskResModel taskResModel = BeanCopierUtil.copy(taskInfo, ExcelTaskResModel.class);
        //添加到缓存-并返回
        redisCache.putString(cacheKey, JSON.toJSONString(taskResModel), ExcelExportConstant.TASK_CACHE_EXPIRED);
        return taskResModel;
    }

    /**
     * 刷新任务缓存
     *
     * @param taskId 任务ID
     */
    public void refreshTaskCache(Integer taskId) {
        //查询最新任务
        ExportTaskInfo taskInfo = exportTaskInfoMapper.getById(taskId);
        if (null == taskInfo) {
            return;
        }
        ExcelTaskResModel taskResModel = BeanCopierUtil.copy(taskInfo, ExcelTaskResModel.class);
        //任务缓存key
        String taskCacheKey = ExcelExportConstant.getTaskCacheKey(taskId);
        //刷新缓存
        redisCache.putString(taskCacheKey, JSON.toJSONString(taskResModel), ExcelExportConstant.TASK_CACHE_EXPIRED);
    }

    /**
     * 上传文件到-OSS
     *
     * @param tempTaskFilePath
     * @param tempExcelFilePath
     * @param fileName
     */
    private String doUploadOss(String tempTaskFilePath, String tempExcelFilePath, String fileName, boolean neverExpire) {
        List<String> excelFileList = FileUtil.listFileNames(tempExcelFilePath);
        if (CollectionUtils.isEmpty(excelFileList)) {
            throw new RuntimeException("导出失败,临时文件不存在");
        }
        //上传文件名
        String uploadFileName;
        //上传文件本地位置
        String uploadFileLocalPath;
        //上传压缩包文件
        if (excelFileList.size() > ExcelExportConstant.ONE) {
            uploadFileLocalPath = ExcelExportConstant.getTempZipFullFileName(tempTaskFilePath, fileName);
            //压缩excel文件夹
            ZipUtil.zip(tempExcelFilePath, uploadFileLocalPath);
            //创建云存储服务器文件名 示例：（oss存储）文件名_yyyyMMddHHmmss+4位随机数
            uploadFileName = ExcelExportConstant.getCloudZipFileName(fileName, ossProperty.getExportExcelDir());
            return this.doUploadOss(uploadFileName, uploadFileLocalPath, neverExpire);
        }
        //上传单个excel文件
        //获取临时目录下第一个文件
        String excelFileName = excelFileList.get(ExcelExportConstant.ZERO);
        //当前临时excel文件全路径
        uploadFileLocalPath = ExcelExportConstant.getTempExcelFullFileName(tempExcelFilePath, excelFileName);
        //创建云存储服务器文件名 示例：（oss存储）文件名_yyyyMMddHHmmss+4位随机数
        uploadFileName = ExcelExportConstant.getCloudExcelFileName(fileName, ossProperty.getExportExcelDir());
        return this.doUploadOss(uploadFileName, uploadFileLocalPath, neverExpire);
    }

    /**
     * 获取文件访问地址
     *
     * @param uploadFileName
     * @param neverExpire
     */
    private String doUploadOss(String uploadFileName, String uploadFileLocalPath, boolean neverExpire) {
        //OSS客户端-上传
        OSS ossClient = this.getAliOssConfig(neverExpire);
        Map<String, String> tags = this.getTags(neverExpire);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setObjectTagging(tags);

        AliOssUtil.uploadFile(ossProperty.getBucket(), uploadFileName, uploadFileLocalPath, ossClient, metadata);
        //自定义效期
        return URLUtil.encode(AliOssUtil.getFileUrl(ossProperty.getEndpoint(), ossProperty.getBucket(), uploadFileName));
    }

    /**
     * 新增任务
     */
    private Integer doAddTask(ExcelExportBizTypeEnum exportEnum, BasePageModel param) {
        ExportTaskInfo taskInfo = new ExportTaskInfo();
        //业务类型
        taskInfo.setBizType(exportEnum.getBizType());
        //任务标题
        taskInfo.setTaskTitle(exportEnum.getFileName());
        //任务状态：执行中
        taskInfo.setTaskStatus(ExportTaskStatusEnum.IN_EXECUTE.getValue());
        //任务参数
        taskInfo.setTaskParam("");
        //非永久有效下-设置有效期时间
        if (!exportEnum.isNeverExpire()) {
            //效期3天
            taskInfo.setExpireTime(DateUtil.offsetDay(new Date(), 3));
        }

        taskInfo.setOperateBy(param.getOperateBy());
        taskInfo.setOperateName(param.getOperateName());
        taskInfo.setTaskParam(JSON.toJSONString(param));
        //新增任务
        exportTaskInfoMapper.insertTask(taskInfo);
        return taskInfo.getId();
    }

    /**
     * 任务执行-成功更新
     *
     * @param taskId 任务ID
     */
    public int updateSuccess(Long taskDual, Integer taskId, String fileUrl, Integer taskRows) {
        return exportTaskInfoMapper.updateSuccess(taskDual, taskId, fileUrl, taskRows, ExportTaskStatusEnum.SUCCESS.getValue());
    }

    /**
     * 任务执行-失败更新
     *
     * @param taskId 任务ID
     */
    public int updateFail(Long taskDual, Integer taskId, String remarks) {
        if (!StringUtils.isEmpty(remarks) && remarks.length() > ExcelExportConstant.MAX_ERROR) {
            remarks = remarks.substring(ExcelExportConstant.ZERO, ExcelExportConstant.MAX_ERROR);
        }
        return exportTaskInfoMapper.updateFail(taskDual, taskId, remarks, ExportTaskStatusEnum.FAIL.getValue());
    }

    public OSS getAliOssConfig(boolean neverExpire) {
        OSS ossClient = new OSSClientBuilder().build(ossProperty.getEndpoint(), ossProperty.getAccessId(), ossProperty.getAccessKey());
        // 设置规则ID、文件匹配前缀与标签。
        String ruleId = ossProperty.getExportExcelDir();
        String matchPrefix0 = ossProperty.getExportExcelDir();
        Map<String, String> matchTags = this.getTags(neverExpire);

        if (!neverExpire) {
            // 创建SetBucketLifecycleRequest。
            SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(ossProperty.getBucket());
            // 距最后修改时间3天后过期。
            LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix0, LifecycleRule.RuleStatus.Enabled, ossProperty.getGenExcelExpireDay());
            rule.setTags(matchTags);
            request.AddLifecycleRule(rule);
            // 发起设置生命周期规则请求。
            ossClient.setBucketLifecycle(request);
        }

        return ossClient;
    }

    private Map<String, String> getTags(boolean neverExpire) {
        Map<String, String> tags = new HashMap<>();
        //永不过期
        if (neverExpire) {
            tags.put("neverExpire", "neverExpire");
        } else {
            tags.put("threeDay", "threeDay");
        }
        return tags;
    }
}
