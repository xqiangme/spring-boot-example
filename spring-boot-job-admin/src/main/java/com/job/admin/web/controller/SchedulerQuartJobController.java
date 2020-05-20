package com.job.admin.web.controller;

import com.job.admin.service.ScheduledQuartzJobService;
import com.job.admin.shiro.ShiroOperation;
import com.job.admin.web.param.Response;
import com.job.admin.web.param.bo.*;
import com.job.admin.web.param.vo.ScheduledQuartzJobPageVO;
import com.job.admin.web.param.vo.UserLoginResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 任务管理 Controller
 *
 * @author mengqiang
 */
@RestController
@RequestMapping("/job")
public class SchedulerQuartJobController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private ScheduledQuartzJobService scheduledQuartzJobService;

    /**
     * 首页统计
     *
     * @author mengq
     */
    @RequestMapping("/getHomeCount")
    public Response getHomeCount() {
        return Response.success(scheduledQuartzJobService.getHomeCount());
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
        ScheduledQuartzJobPageVO result = scheduledQuartzJobService.listPageJob(queryBO);
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
        return Response.success(scheduledQuartzJobService.getJobDetail(queryBO));
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
        scheduledQuartzJobService.startJob(operateBO);
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
        scheduledQuartzJobService.stopJob(operateBO);
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
        scheduledQuartzJobService.updateJob(updateBO);
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
        scheduledQuartzJobService.addJob(jobSaveBO);
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
        scheduledQuartzJobService.deleteJob(operateBO);
        return Response.success();
    }

    private void buildOperate(JobBaseOperateBO operateBO) {
        UserLoginResult currentUser = ShiroOperation.getCurrentUser();
        if (null != currentUser) {
            operateBO.setOperateBy(String.valueOf(currentUser.getUserId()));
            operateBO.setOperateName(StringUtils.isBlank(currentUser.getRealName()) ?
                    currentUser.getUsername() : currentUser.getRealName());
            operateBO.setClientIp(StringUtils.isBlank(currentUser.getClientIp()) ? "" : currentUser.getClientIp());
            operateBO.setIpAddress(StringUtils.isBlank(currentUser.getIpAddress()) ? "" : currentUser.getIpAddress());
            operateBO.setBrowserName(StringUtils.isBlank(currentUser.getBrowserName()) ? "" : currentUser.getBrowserName());
            operateBO.setOs(StringUtils.isBlank(currentUser.getOs()) ? "" : currentUser.getOs());
        }
    }
}
