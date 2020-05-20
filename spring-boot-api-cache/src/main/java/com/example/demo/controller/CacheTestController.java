package com.example.demo.controller;

import com.example.demo.bean.UserInfo;
import com.example.demo.param.UserListQueryBO;
import com.example.demo.param.UserListQueryBO2;
import com.example.demo.result.Response;
import com.example.demo.service.base.UserCacheBaseService;
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
 */
@RestController
@RequestMapping("/cache-user")
public class CacheTestController {

    @Autowired
    private UserCacheBaseService userCacheBaseService;

    /**
     * 无参数方法测试-查询全部用户
     */
    @ResponseBody
    @RequestMapping("/listAll")
    public Response listAll() {
        return Response.success(userCacheBaseService.listAll());
    }

    /**
     * 根据用户ID查询
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserById")
    public Response getUserById(Integer id) {
        return Response.success(userCacheBaseService.getUserById(id));
    }

    /**
     * 根据用户ID查询
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserByIdAndStatus")
    public Response getUserByIdAndStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        return Response.success(userCacheBaseService.getUserByIdAndStatus(id, status));
    }

    /**
     * 根据用户ID查询-可存储null值
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserByIdStorageNull")
    public Response getUserByIdStorageNull(Integer id) {
        return Response.success(userCacheBaseService.getUserByIdStorageNull(id));
    }

    /**
     * 普通参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     */
    @ResponseBody
    @RequestMapping("/listPageUser")
    public Response listPageUser(Integer page, Integer pageSize) {
        return Response.success(userCacheBaseService.listPageUser(page, pageSize));
    }

    /**
     * 对象参数测试-分页查询用户
     *
     * @param queryBO
     */
    @ResponseBody
    @RequestMapping("/listPageUserByObjParam")
    public Response listPageUserByObjParam(UserListQueryBO queryBO) {
        return Response.success(userCacheBaseService.listPageUserByObjParam(queryBO));
    }

    /**
     * 普通参数+对象参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     * @param queryBO
     */
    @ResponseBody
    @RequestMapping("/listPageUserByObjAndParam")
    public Response listPageUserByObjAndParam(Integer page, Integer pageSize, UserListQueryBO2 queryBO) {
        return Response.success(userCacheBaseService.listPageUserByObjAndParam(page, pageSize, queryBO));
    }

    /**
     * 不使用缓存直接执行方法
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserById-notReadCacheFlag")
    public Response getUserByIdNotReadCache(Integer id) {
        //自定义设置不走缓存
        CacheContext.notReadCacheFlag.set(true);
        UserInfo userInfo = userCacheBaseService.getUserById(id);
        CacheContext.notReadCacheFlag.set(false);
        return Response.success(userInfo);
    }

    /**
     * 不使用缓存-查询并更新缓存
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping("/getUserById-refreshCache")
    public Response getUserByIdRefreshCache(Integer id) {
        //自定义设置-刷新缓存
        CacheContext.refreshCacheFlag.set(true);
        UserInfo userInfo = userCacheBaseService.getUserById(id);
        CacheContext.refreshCacheFlag.set(false);
        return Response.success(userInfo);
    }

}
