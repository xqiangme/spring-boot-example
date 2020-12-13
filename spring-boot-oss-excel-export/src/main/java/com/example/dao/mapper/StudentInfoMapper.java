package com.example.dao.mapper;

import com.example.dao.entity.StudentInfo;
import com.example.dao.query.StudentInfoQuery;

import java.util.List;

/**
 * 学生表
 */
public interface StudentInfoMapper {

    /**
     * 测试查询全部方法
     */
    List<StudentInfo> listAll();


    /**
     * 查询学生列表
     *
     * @param query 学生
     * @return 学生集合
     */
    int countByCondition(StudentInfoQuery query);

    /**
     * 查询学生列表
     *
     * @param query 学生
     * @return 学生集合
     */
    List<StudentInfo> listPageByCondition(StudentInfoQuery query);

}
