package com.example.admin.web.controller;

import com.example.admin.web.param.bo.*;
import com.example.admin.web.param.Response;
import com.example.admin.web.param.vo.UserLoginResult;
import com.example.admin.web.param.vo.UserInfoPageVO;
import com.example.admin.service.ScheduledQuartUserService;
import com.example.admin.shiro.ShiroOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mengqiang
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class ScheduledQuartUserController {

    @Resource
    private ScheduledQuartUserService scheduledQuartUserService;

    /**
     * 分页列表
     *
     * @param queryBO
     * @return 分页列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public Response listPageJob(JobTaskUserPageQueryBO queryBO) {
        UserInfoPageVO result = scheduledQuartUserService.listPage(queryBO);
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
    @RequestMapping("/getUserDetail")
    public Response getUserDetail(@RequestBody JobTaskUserOperateBO queryBO) {
        return Response.success(scheduledQuartUserService.getUserDetail(queryBO));
    }

    /**
     * 用户详情
     *
     * @return 用户个人详情
     * @author mengq
     */
    @RequestMapping("/getUserPersonDetail")
    public Response getUserPersonDetail() {
        return Response.success(scheduledQuartUserService.getUserPersonDetail());
    }

    /**
     * 新增用户
     *
     * @param saveBO
     * @author mengq
     */
    @RequestMapping("/save")
    public Response saveUser(@RequestBody JobTaskUserSaveBO saveBO) {
        this.buildOperate(saveBO);
        scheduledQuartUserService.saveUser(saveBO);
        return Response.success();
    }

    /**
     * 修改用户
     *
     * @param updateBO
     * @author mengq
     */
    @RequestMapping("/update")
    public Response updateUser(@RequestBody JobTaskUserUpdateBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartUserService.updateUser(updateBO);
        return Response.success();
    }

    /**
     * 修改密码
     *
     * @param updateBO
     * @author mengq
     */
    @RequestMapping("/update-pwd")
    public Response updatePwd(@RequestBody JobTaskUserUpdatePwdBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartUserService.updatePwd(updateBO);
        return Response.success();
    }

    /**
     * 修改权限
     *
     * @param updateBO
     * @author mengq
     */
    @RequestMapping("/update-power")
    public Response updatePower(@RequestBody JobTaskUserUpdatePowerBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartUserService.updateUserPower(updateBO);
        return Response.success();
    }

    /**
     * 删除用户
     *
     * @param saveBO
     * @author mengq
     */
    @RequestMapping("/delete")
    public Response deleteUser(@RequestBody JobTaskUserOperateBO saveBO) {
        this.buildOperate(saveBO);
        scheduledQuartUserService.deleteUser(saveBO);
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
