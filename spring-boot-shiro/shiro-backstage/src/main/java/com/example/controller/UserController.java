package com.example.controller;

import com.example.model.result.Response;
import com.example.shiro.ShiroOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 人员列表
     */
    @PostMapping("/list")
    public Response userList() {
        log.info("[人员管理] >> userList");
        return Response.success();
    }

    /**
     * 人员新增
     */
    @PostMapping("/add")
    public Response userAdd() {
        log.info("[人员管理] >> userAdd");
        return Response.success();
    }


    /**
     * 人员列表
     */
    @PostMapping("/del")
    public Response userDel() {
        log.info("[人员管理] >> userDel");
        return Response.success();
    }

    /**
     * 人员列表
     */
    @PostMapping("/detail")
    public Response userDetail() {
        log.info("[人员管理] >> userDetail");
        return Response.success();
    }

}
