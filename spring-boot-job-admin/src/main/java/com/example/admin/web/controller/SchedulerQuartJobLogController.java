package com.example.admin.web.controller;

import com.example.admin.web.param.bo.JobTaskLogDetailBO;
import com.example.admin.web.param.bo.JobTaskLogPageQueryBO;
import com.example.admin.web.param.Response;
import com.example.admin.web.param.vo.ScheduledQuartzJobLogPageVO;
import com.example.admin.service.TaskJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务日志controller
 *
 * @author mengqiang
 */
@Slf4j
@RestController
@RequestMapping("/job-log")
public class SchedulerQuartJobLogController {

    @Resource
    private TaskJobLogService taskJobLogService;

    /**
     * 分页任务列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public Response listPageJob(JobTaskLogPageQueryBO queryBO) {
        ScheduledQuartzJobLogPageVO result = taskJobLogService.listPageLog(queryBO);
        Response response = Response.success(result.getList());
        response.setCount(result.getTotal());
        return response;
    }

    /**
     * 日志详情
     *
     * @param detailBO
     * @return 日志详情
     * @author mengq
     */
    @RequestMapping("/getLogDetail")
    public Response getJobDetail(@RequestBody JobTaskLogDetailBO detailBO) {
        return Response.success(taskJobLogService.getLogDetail(detailBO));
    }

}
