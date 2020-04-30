package com.example.demo.service.biz;

import com.example.demo.bean.UserInfo;
import com.example.demo.param.UserListQueryBO;
import com.example.demo.param.UserListQueryBO2;

import java.util.List;

/**
 * 业务处理Service
 *
 * @author mengq
 * @version 1.0
 */
public interface UserBizService {

    /**
     * 根据用户ID查询
     *
     * @param id
     * @return
     */
    UserInfo getUserById(Integer id);

}
