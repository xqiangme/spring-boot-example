package com.example.demo.service.biz.impl;

import com.example.aspect.CacheAspect;
import com.example.demo.bean.UserInfo;
import com.example.demo.service.base.UserCacheBaseService;
import com.example.demo.service.biz.UserBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务处理Service
 *
 * @author mengq
 * @version 1.0
 */
@Service
public class UserBizServiceImpl implements UserBizService {

    private static final Logger log = LoggerFactory.getLogger(UserBizServiceImpl.class);


    @Autowired
    private UserCacheBaseService userCacheBaseService;

    @Override
    public UserInfo getUserById(Integer id) {
        log.info("UserBizServiceImpl >> getUserById id:{}}", id);
        return userCacheBaseService.getUserById(id);
    }
}
