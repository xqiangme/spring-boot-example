package com.example.controller;

import com.example.bean.param.MenuAddParam;
import com.example.bean.param.UserAddParam;
import com.example.bean.result.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 菜单管理接口
 *
 * @author 码农猿
 */
@RestController
@RequestMapping("/menu")
public class MenuInfoController {

    /**
     * 添加菜单
     *
     * @author 码农猿
     */
    @PostMapping(value = "/add-menu")
    public Response addMenu(@RequestBody MenuAddParam addParam) {
        return Response.success();
    }


}