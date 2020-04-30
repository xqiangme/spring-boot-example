package com.example.admin.web.controller;

import com.example.admin.web.param.bo.*;
import com.example.admin.web.param.Response;
import com.example.admin.web.param.vo.UserLoginResult;
import com.example.admin.web.param.vo.ScheduledQuartzJobPageVO;
import com.example.admin.service.TaskJobService;
import com.example.admin.shiro.ShiroOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务管理 Controller
 *
 * @author mengqiang
 */
@RestController
@RequestMapping("/job")
public class SchedulerQuartJobController {

    @Resource
    private TaskJobService taskJobService;

    /**
     * 首页统计
     *
     * @author mengq
     */
    @RequestMapping("/getHomeCount")
    public Response getHomeCount() {
        return Response.success(taskJobService.getHomeCount());
    }


    /**
     * 分页任务列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public Response listPageJob(JobTaskPageQueryBO queryBO) {
        ScheduledQuartzJobPageVO result = taskJobService.listPageJob(queryBO);
        Response response = Response.success(result.getList());
        response.setCount(result.getTotal());
        return response;
    }

    /**
     * 任务详情
     *
     * @param queryBO
     * @return 任务详情
     * @author mengq
     */
    @RequestMapping("/getJobDetail")
    public Response getJobDetail(@RequestBody JobTaskOperateBO queryBO) {
        return Response.success(taskJobService.getJobDetail(queryBO));
    }

    /**
     * 启动任务
     *
     * @param operateBO
     * @author mengq
     */
    @RequestMapping("/start")
    public Response startJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        taskJobService.startJob(operateBO);
        return Response.success();
    }

    /**
     * 停止任务
     *
     * @param operateBO
     * @author mengq
     */
    @RequestMapping("/stop")
    public Response stopJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        taskJobService.stopJob(operateBO);
        return Response.success();
    }

    /**
     * 修改任务
     *
     * @param updateBO
     */
    @RequestMapping("/update")
    public Response updateJob(@RequestBody JobTaskUpdateBO updateBO) {
        this.buildOperate(updateBO);
        taskJobService.updateJob(updateBO);
        return Response.success();
    }

    /**
     * 新增任务
     *
     * @param jobSaveBO
     * @author mengq
     */
    @RequestMapping("/save")
    public Response addJob(@RequestBody JobTaskSaveBO jobSaveBO) {
        this.buildOperate(jobSaveBO);
        taskJobService.addJob(jobSaveBO);
        return Response.success();
    }

    /**
     * 删除任务
     *
     * @param operateBO
     */
    @RequestMapping("/delete")
    public Response deleteJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        taskJobService.deleteJob(operateBO);
        return Response.success();
    }

    private void buildOperate(JobBaseOperateBO operateBO) {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null != currentUser) {
            operateBO.setOperateBy(String.valueOf(currentUser.getUserId()));
            operateBO.setOperateName(StringUtils.isBlank(currentUser.getRealName()) ?
                    currentUser.getUsername() : currentUser.getRealName());
        }
    }
}
