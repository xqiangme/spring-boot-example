package com.example.demo.controller;

import com.example.demo.bean.UserInfo;
import com.example.demo.param.UserListQueryBO;
import com.example.demo.param.UserListQueryBO2;
import com.example.demo.result.Response;
import com.example.demo.service.base.UserCacheBaseService;
import com.example.demo.service.biz.UserBizService;
import com.example.util.CacheContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 controller
 *
 * @author mengq
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserBizTestController {

    @Autowired
    private UserBizService userBizService;

    /**
     * 根据用户ID查询
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserById")
    public Response getUserById(Integer id) {
        return Response.success(userBizService.getUserById(id));
    }

}

