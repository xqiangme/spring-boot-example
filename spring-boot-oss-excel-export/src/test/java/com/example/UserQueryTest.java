package com.example;

import com.example.dao.entity.UserInfo;
import com.example.dao.mapper.UserInfoMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 人员 查询测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserQueryTest {

    @Autowired
    private UserInfoMapper userInfo1Mapper;


    @org.junit.Test
    public void testQuery() throws Exception {
        UserInfo user1 = userInfo1Mapper.getByUserId("1001");
        System.out.println();
        System.out.println("********* 分隔符 *********");
        System.out.println(user1);
    }


}