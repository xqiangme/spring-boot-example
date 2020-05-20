package com.job.admin.dao.mapper;

import com.job.admin.dao.bean.ScheduledQuartUserInfo;
import com.job.admin.dao.dto.JobUserPageQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 人员管理
 *
 * @author mengq
 */
@Repository
public interface ScheduledQuartzUserMapper {

    /**
     * 新增项目下配置
     *
     * @param configInfo
     */
    void addUser(ScheduledQuartUserInfo configInfo);


    /**
     * 根据ID更新
     *
     * @param configInfo
     * @return 处理行数
     */
    int updateSelectiveById(ScheduledQuartUserInfo configInfo);

    /**
     * 移除
     *
     * @param id
     * @param projectKey
     * @param updateBy
     * @return 处理行数
     */
    int deleteByProjectAndId(@Param("projectKey") String projectKey, @Param("id") Integer id,
                             @Param("updateBy") String updateBy);

    /**
     * 根据ID查询任务
     *
     * @param id
     * @return
     */
    ScheduledQuartUserInfo getById(@Param("id") Integer id);

    /**
     * 根据项目与用户名查询
     *
     * @param username
     * @param projectKey
     * @return
     */
    ScheduledQuartUserInfo getByProjectAndUsername(@Param("projectKey") String projectKey, @Param("username") String username);

    /**
     * 根据项目与用户名统计
     *
     * @param projectKey
     * @param username
     * @return
     */
    int countByProjectAndUsername(@Param("projectKey") String projectKey, @Param("username") String username);

    /**
     * 组和名称相同的任务-排除自己
     *
     * @param projectKey
     * @param username
     * @param excludeId
     * @return
     */
    int countByProjectAndUsernameExcludeId(@Param("projectKey") String projectKey,
                                           @Param("username") String username,
                                           @Param("excludeId") Integer excludeId);

    /**
     * 根据条件统计
     *
     * @param queryBO
     * @return
     */
    Integer countByCondition(JobUserPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO
     * @return
     */
    List<ScheduledQuartUserInfo> listPageByCondition(JobUserPageQueryDTO queryBO);

}
