package com.example.controller;

import com.example.anno.CurrentUser;
import com.example.model.UserAddParam;
import com.example.model.UserLoginModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 人员管理接口
 *
 * @author 码农猿
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {


    /**
     * 添加人员
     *
     * @author 码农猿
     */
    @PostMapping(value = "/add-user")
    public String addUser(@CurrentUser UserLoginModel userLoginModel, @RequestBody UserAddParam userAddParam) {
        log.info("[业务处理] >> UserInoController >> addUser  >> CurrentUser : {}", userLoginModel);

        log.info("[业务处理] CurrentUser : {}", userLoginModel);
        log.info("[业务处理] userAddParam : {}", userAddParam);

        return "success";
    }

    /**
     * 人员详情
     *
     * @author 码农猿
     */
    @PostMapping(value = "/get-user")
    public String getUser(@RequestParam("userId") String userId) {
        log.info("[业务处理] >> UserInoController >> getUser  >> userId : {}", userId);

        return "success";
    }

}