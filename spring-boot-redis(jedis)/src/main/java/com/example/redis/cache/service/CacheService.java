/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.redis.cache.service;

import com.example.redis.cache.annotation.CacheData;

import java.util.List;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-24 21:24
 * @desc
 */
public interface CacheService {

    List<Object> getTest1(String type);

    @CacheData(expireTime = 20 * 60, storageNullFlag = true)
    List<Object> getTest2(String type);
}
