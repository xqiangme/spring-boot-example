package com.example.mapper.db2;

import com.example.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测试 数据源 2
 * <p>
 * 人员信息mapper接口
 *
 * @author 码农猿
 * @date 2019-03-25 23:10:08
 */
public interface UserInfo2Mapper {


    /**
     * 新增接口
     *
     * @param userInfo 人员信息实体
     * @return 新增的行数
     * @author 码农猿
     * @date 2019-03-25 23:10:08
     */
    int insert(UserInfo userInfo);


    /**
     * 查询所有 人员信息
     *
     * @return 人员信息 列表
     * @author 码农猿
     * @date 2019-03-25 23:10:08
     */
    List<UserInfo> listAll();

    /**
     * 根据业务主键id查询
     *
     * @param userId 业务主键
     * @return 人员信息实体
     * @author 码农猿
     * @date 2019-03-25 23:10:08
     */
    UserInfo getByUserId(String userId);

    /**
     * 根据业务主键编辑
     *
     * @param userInfo 人员信息实体
     * @return 编辑的行数
     * @author 码农猿
     * @date 2019-03-25 23:10:08
     */
    int updateByUserId(UserInfo userInfo);

    /**
     * 根据业务主键动态参数编辑
     *
     * @param userInfo 人员信息实体
     * @return 编辑的行数
     * @author 码农猿
     * @date 2019-03-25 23:10:08
     */
    int updateByUserIdSelective(UserInfo userInfo);


}
