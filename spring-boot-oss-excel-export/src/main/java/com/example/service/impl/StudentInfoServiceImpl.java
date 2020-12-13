/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.comment.ExcelExportComment;
import com.example.common.enums.ExcelExportBizTypeEnum;
import com.example.dao.entity.StudentInfo;
import com.example.dao.mapper.StudentInfoMapper;
import com.example.dao.query.StudentInfoQuery;
import com.example.service.StudentInfoService;
import com.example.util.BeanCopierUtil;
import com.example.util.PageUtils;
import com.example.web.param.StudentInfoParam;
import com.example.web.vo.StudentExportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程序员小强
 * @date 2020-11-08 17:24
 * @desc
 */
@Slf4j
@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private StudentInfoMapper studentInfoMapper;
    @Resource
    private ExcelExportComment excelExportComment;


    @Override
    public Integer exportStudentList(StudentInfoParam param) {
        //调用通用导出组件-返回任务ID，异步执行分页导出
        return excelExportComment.invoke((a) -> exportStudent(param), param, ExcelExportBizTypeEnum.STUDENT_INFO, StudentExportVO.class);
    }

    private StudentInfoQuery buildStudentInfoQuery(StudentInfoParam param) {
        StudentInfoQuery userInfoQuery = new StudentInfoQuery();
        userInfoQuery.setClassId(param.getClassId());
        userInfoQuery.setStartRow(PageUtils.getStartRow(param.getPage(), param.getPageSize()));
        userInfoQuery.setPageSize(param.getPageSize());
        if (!StringUtils.isEmpty(param.getCreateStartTime())) {
            userInfoQuery.setCreateStartTime(DateUtil.beginOfDay(DateUtil.parseTime(param.getCreateStartTime())));
        }
        if (!StringUtils.isEmpty(param.getCreateEndTime())) {
            userInfoQuery.setCreateEndTime(DateUtil.endOfDay(DateUtil.parseTime(param.getCreateEndTime())));
        }
        return userInfoQuery;

    }

    /**
     * 写好分页接口
     *
     * @param param
     */
    private List<StudentExportVO> exportStudent(StudentInfoParam param) {
        StudentInfoQuery studentInfoQuery = this.buildStudentInfoQuery(param);
        List<StudentInfo> userInfoList = studentInfoMapper.listPageByCondition(studentInfoQuery);
        if (CollectionUtils.isEmpty(userInfoList)) {
            return new ArrayList<>(0);
        }

        return BeanCopierUtil.copyList(userInfoList, StudentExportVO.class);
    }
}
