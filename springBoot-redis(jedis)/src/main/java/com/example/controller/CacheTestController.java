/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.controller;

import com.example.redis.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-24 21:32
 * @desc
 */
@RestController
public class CacheTestController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/getTest1")
    @ResponseBody
    public Object getTest1(String type) {
        return cacheService.getTest1(type);
    }

    @RequestMapping("/getTest2")
    @ResponseBody
    public Object getTest2(String type) {
        return cacheService.getTest2(type);
    }
}
