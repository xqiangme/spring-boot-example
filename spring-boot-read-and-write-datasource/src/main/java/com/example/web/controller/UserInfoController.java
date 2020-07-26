
package com.example.web.controller;

import com.example.entity.UserInfo;
import com.example.service.UserService;
import com.example.web.bo.UserAddBO;
import com.example.web.bo.UserUpdateBO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 读写分离-Demo人员测试接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserService userService;

    /**
     * 新增人员
     *
     * @param addBO
     */
    @PostMapping("/add")
    public Object addUser(@RequestBody UserAddBO addBO) {
        userService.addUser(addBO);
        return "success";
    }

    /**
     * 修改人员
     *
     * @param updateBO
     */
    @PostMapping("/update")
    public Object updateUser(@RequestBody UserUpdateBO updateBO) {
        userService.updateUser(updateBO);
        return "success";
    }

    /**
     * 查询所有 人员信息
     *
     * @return 人员信息 列表
     */
    @RequestMapping("/listAll")
    public List<UserInfo> getAll() {
        return userService.getAll();
    }

    /**
     * 根据业务主键ID查询
     *
     * @param userId 业务主键
     */
    @RequestMapping("/getByUserId")
    public UserInfo getByUserId(@RequestParam("userId") String userId) {
        return userService.getByUserId(userId);
    }
}