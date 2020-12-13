package com.example.dao.mapper;

import com.example.dao.entity.ExportTaskInfo;
import org.apache.ibatis.annotations.Param;


/**
 * 导出任务表 Mapper
 */
public interface ExportTaskInfoMapper {

    /**
     * 根据任务ID查询数据
     *
     * @param id
     * @return ExportTaskInfo
     */
    ExportTaskInfo getById(@Param("id") Integer id);

    /**
     * 新增任务
     *
     * @param entity entity
     * @return int
     */
    int insertTask(ExportTaskInfo entity);

    /**
     * 任务成功更新
     *
     * @param taskDual   taskDual
     * @param id         taskId
     * @param fileUrl    fileUrl
     * @param taskRows   taskRows
     * @param taskStatus taskStatus
     * @return int
     */
    int updateSuccess(@Param("taskDual") Long taskDual, @Param("id") Integer id, @Param("fileUrl") String fileUrl, @Param("taskRows") Integer taskRows, @Param("taskStatus") Integer taskStatus);

    /**
     * 任务失败更新
     *
     * @param taskDual   taskDual
     * @param id         taskId
     * @param remarks    remarks
     * @param taskStatus taskStatus
     * @return int
     */
    int updateFail(@Param("taskDual") Long taskDual, @Param("id") Integer id, @Param("remarks") String remarks, @Param("taskStatus") Integer taskStatus);
}
