package com.example.elasticsearch;

import com.example.elasticsearch.bean.UserInfo;
import com.example.elasticsearch.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * ElasticSearch 文档-增删改测试
 *
 * @author 程序员小强
 */
@SpringBootTest(classes = Application.class)
public class UserCrudTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UserInfoRepository userInfoRepository;

    //添加数据
    @Test
    public void test3() {
        UserInfo user = new UserInfo();
        user.setUserId(1);
        user.setUserName("张三");
        user.setBirthday(new Date());
        user.setPassword("12345678");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");
        user.setHobbies(hobbies);
        userInfoRepository.save(user);   //调用其中的save方法保存信息
    }


    //获取其中的所有数据
    @Test
    public void test4() {
        Iterable<UserInfo> iterable = userInfoRepository.findAll();
        Iterator<UserInfo> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }


    @Test
    public void test5() {
        List<UserInfo> users = new ArrayList<>();
        UserInfo user = new UserInfo();
        user.setUserId(4);
        user.setUserName("张三");
        user.setBirthday(new Date());
        user.setPassword("12345678");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("台球");
        hobbies.add("足球");
        user.setHobbies(hobbies);

        UserInfo user1 = new UserInfo();
        user1.setUserId(5);
        user1.setUserName("郑元梅");
        user1.setBirthday(new Date());
        user1.setPassword("12345678");
        user1.setHobbies(hobbies);

        users.add(user);
        users.add(user1);
        userInfoRepository.saveAll(users);  //保存List中的所有数据
    }

    //删除指定的数据
    @Test
    public void test6() {
        UserInfo user = new UserInfo();
        user.setUserId(5);
        userInfoRepository.delete(user);
    }

}
