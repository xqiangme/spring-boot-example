package com.example.service;

import com.example.web.param.StudentInfoParam;


/**
 * 学生信息-使用通用导出Demo
 *
 * @author 程序员小强
 * @date 2020-11-06 20:56
 */
public interface StudentInfoService {


    Integer exportStudentList(StudentInfoParam param);

}
