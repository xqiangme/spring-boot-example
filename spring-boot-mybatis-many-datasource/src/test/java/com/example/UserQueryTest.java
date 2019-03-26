package com.example;

import com.example.entity.UserInfo;
import com.example.mapper.db1.UserInfo1Mapper;
import com.example.mapper.db2.UserInfo2Mapper;
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
    private UserInfo1Mapper userInfo1Mapper;
    @Autowired
    private UserInfo2Mapper userInfo2Mapper;


    @org.junit.Test
    public void testQuery() throws Exception {
        UserInfo user1 = userInfo1Mapper.getByUserId("1001");
        UserInfo user2 = userInfo2Mapper.getByUserId("1001");
        System.out.println();
        System.out.println("********* 分隔符 *********");
        System.out.println(user1);
        System.out.println(user2);
    }


}