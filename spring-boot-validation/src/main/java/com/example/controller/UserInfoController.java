package com.example.controller;

import com.example.bean.param.UserAddParam;
import com.example.bean.result.Response;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 人员管理接口
 *
 * @author 码农猿
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
    /**
     * 添加人员
     *
     * @author 码农猿
     */
    @PostMapping(value = "/add-user")
    public Response addUser1(@Valid @RequestBody UserAddParam addParam) {
        return null;
    }

}