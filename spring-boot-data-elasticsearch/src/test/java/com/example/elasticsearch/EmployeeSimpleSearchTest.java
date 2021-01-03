package com.example.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.elasticsearch.bean.EmployeeInfo;
import com.example.elasticsearch.service.EmployeeInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * ElasticSearch 简单查询示例
 *
 * @author 程序员小强
 */
@SpringBootTest(classes = Application.class)
public class EmployeeSimpleSearchTest {

    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 精确查询
     * 根据姓名name 查询
     */
    @Test
    public void findByNameTest() {
        String name = "张三";
        List<EmployeeInfo> employeeList = employeeInfoService.findByName(name);
        System.out.println(JSON.toJSONString(employeeList, SerializerFeature.PrettyFormat));
    }

    /**
     * AND 语句查询
     */
    @Test
    public void findByNameAndAgeTest() {
        String name = "张三";
        Integer age = 20;
        List<EmployeeInfo> employeeList = employeeInfoService.findByNameAndAge(name, age);
        System.out.println(JSON.toJSONString(employeeList, SerializerFeature.PrettyFormat));
    }

    /**
     * OR 语句查询
     */
    @Test
    public void findByNameOrAgeTest() {
        String name = "张三";
        Integer age = 18;
        List<EmployeeInfo> employeeList = employeeInfoService.findByNameOrAge(name, age);
        System.out.println(JSON.toJSONString(employeeList, SerializerFeature.PrettyFormat));
    }

    /**
     * 分页查询
     */
    @Test
    public void findByNameNotTest() {
        String name = "王五";
        int pageNo = 1;
        int pageSize = 5;
        Page<EmployeeInfo> employeePage = employeeInfoService.findByNameNot(name, pageNo, pageSize);
        int totalPages = employeePage.getTotalPages();
        int number = employeePage.getNumber();
        int size = employeePage.getSize();
        List<EmployeeInfo> contentList = employeePage.getContent();

        System.out.println("分页查询");
        System.out.println(String.format("totalPages:%d, number:%d, size:%d", totalPages, number, size));
        System.out.println(JSON.toJSONString(contentList, SerializerFeature.PrettyFormat));
    }

    /**
     * 分页查询
     */
    @Test
    public void findByNameLikeTest() {
        String name = "三";
        int pageNo = 1;
        int pageSize = 5;
        Page<EmployeeInfo> employeePage = employeeInfoService.findByNameLike(name, pageNo, pageSize);
        int totalPages = employeePage.getTotalPages();
        int number = employeePage.getNumber();
        int size = employeePage.getSize();
        List<EmployeeInfo> contentList = employeePage.getContent();

        System.out.println("分页查询");
        System.out.println(String.format("totalPages:%d, number:%d, size:%d", totalPages, number, size));
        System.out.println(JSON.toJSONString(contentList, SerializerFeature.PrettyFormat));
    }


}
