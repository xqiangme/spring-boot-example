package com.job.admin.dao.mapper;

import com.job.admin.dao.bean.ScheduledQuartzJobInfo;
import com.job.admin.dao.dto.JobTaskPageQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 任务表 Mapper
 *
 * @author mengq
 */
@Repository
public interface ScheduledQuartzJobMapper {

    /**
     * 新增任务，初始状态为不启动
     *
     * @param job
     */
    void addByJob(ScheduledQuartzJobInfo job);

    /**
     * 更新任务
     *
     * @param job
     * @return 更新行数
     */
    int updateByProjectAndId(ScheduledQuartzJobInfo job);

    /**
     * 移除任务
     *
     * @param id
     * @param projectKey
     * @return 移除行数
     */
    int removeByProjectAndId(@Param("id") Integer id, @Param("projectKey") String projectKey, @Param("updateBy") String updateBy, @Param("updateName") String updateName);

    /**
     * 根据ID查询任务
     *
     * @param id
     * @return
     */
    ScheduledQuartzJobInfo getJobById(@Param("id") Integer id);


    /**
     * 根据项目与ID查询任务
     *
     * @param id
     * @param projectKey
     * @return
     */
    ScheduledQuartzJobInfo getJobByProjectAndId(@Param("id") Integer id, @Param("projectKey") String projectKey);


    /**
     * 根据条件统计
     *
     * @param queryBO
     * @return
     */
    Integer countByCondition(JobTaskPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO
     * @return
     */
    List<ScheduledQuartzJobInfo> listPageByCondition(JobTaskPageQueryDTO queryBO);

    /**
     * 根据项目key与任务状态任务
     *
     * @param projectKey
     * @param jobStatus
     * @return
     */
    List<ScheduledQuartzJobInfo> getJobListByProjectAndStatus(@Param("projectKey") String projectKey, @Param("jobStatus") Integer jobStatus);


    /**
     * 查询project下，组和名称相同的任务
     *
     * @param projectKey
     * @param jobGroup
     * @param jobName
     * @return
     */
    List<ScheduledQuartzJobInfo> getJobListByProjectGroupAndName(@Param("projectKey") String projectKey,
                                                                 @Param("jobGroup") String jobGroup,
                                                                 @Param("jobName") String jobName);

    /**
     * 统计project下，组和名称相同的任务
     *
     * @param projectKey
     * @param jobGroup
     * @param jobClass
     * @param jobMethod
     * @return
     */
    int countByProjectGroupAndMethod(@Param("projectKey") String projectKey,
                                     @Param("jobGroup") String jobGroup,
                                     @Param("jobClass") String jobClass,
                                     @Param("jobMethod") String jobMethod);

    /**
     * 查询project下，组和名称相同的任务-排除自己
     *
     * @param projectKey
     * @param jobGroup
     * @param jobClass
     * @param jobMethod
     * @param excludeJobId
     * @return 统计值
     */
    int countByProjectGroupAndMethodExclude(@Param("projectKey") String projectKey,
                                            @Param("jobGroup") String jobGroup,
                                            @Param("jobClass") String jobClass,
                                            @Param("jobMethod") String jobMethod,
                                            @Param("excludeJobId") Integer excludeJobId);


    /**
     * 根据项目与状态统计
     *
     * @param projectKey
     * @param jobStatus
     * @return
     */
    int countByProjectAndStatus(@Param("projectKey") String projectKey, @Param("jobStatus") Integer jobStatus);


}
