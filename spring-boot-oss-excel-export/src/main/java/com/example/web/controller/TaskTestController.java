
package com.example.web.controller;

import com.example.comment.ExcelExportComment;
import com.example.common.ExcelTaskResModel;
import com.example.service.StudentInfoService;
import com.example.service.UserService;
import com.example.web.param.StudentInfoParam;
import com.example.web.param.UserInfoParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 下载中心-任务测试接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/task")
public class TaskTestController {

    @Resource
    private UserService userService;
    @Resource
    private StudentInfoService studentInfoService;
    @Resource
    private ExcelExportComment excelExportComment;

    @RequestMapping("/getTaskById")
    public ExcelTaskResModel getTaskById(@RequestParam(value = "taskId", required = true) Integer taskId) {
        return excelExportComment.getTaskById(taskId);
    }

    @RequestMapping("/exportUser")
    public Object exportUserList(UserInfoParam param) {
        param.setOperateBy("system");
        param.setOperateName("system");
        return userService.exportUserList(param);
    }

    @RequestMapping("/exportStudent")
    public Object exportStudentList(StudentInfoParam param) {
        param.setOperateBy("system");
        param.setOperateName("system");
        return studentInfoService.exportStudentList(param);
    }
    
}