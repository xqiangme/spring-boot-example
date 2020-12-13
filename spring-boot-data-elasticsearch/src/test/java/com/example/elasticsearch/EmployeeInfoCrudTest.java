package com.example.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.elasticsearch.bean.EmployeeInfo;
import com.example.elasticsearch.service.EmployeeInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * ElasticSearch 文档-增删改测试
 *
 * @author 程序员小强
 */
@SpringBootTest(classes = Application.class)
public class EmployeeInfoCrudTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 判断文档是否存在
     */
    @Test
    public void exists() {
        String id = "11";
        boolean exists = employeeInfoService.exists(id);
        System.out.println(exists);
    }

    /**
     * 新增
     */
    @Test
    public void save() throws Exception {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setId(1001L);
        employeeInfo.setJobNo("2001");
        employeeInfo.setName("张三");
        employeeInfo.setEnglishName("zhangsan");
        employeeInfo.setJob("Java");
        employeeInfo.setSex(1);
        employeeInfo.setAge(20);
        employeeInfo.setSalary(new BigDecimal("12500.01"));
        employeeInfo.setJobDay(DATE_FORMAT.parse("2019-09-10"));
        employeeInfo.setRemark("我是备注");

        employeeInfoService.saveOrUpdate(employeeInfo);
        System.out.println("新增员工数据结束:");
        System.out.println(JSON.toJSONString(employeeInfo, SerializerFeature.PrettyFormat));
    }

    /**
     * 修改
     */
    @Test
    public void update() throws Exception {
        Long id = 1001L;

        EmployeeInfo employeeInfo1 = employeeInfoService.findById(id);
        System.out.println("修改前:");
        System.out.println(JSON.toJSONString(employeeInfo1, SerializerFeature.PrettyFormat));

        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setId(1001L);
        employeeInfo.setJobNo("2001");
        employeeInfo.setName("张三");
        employeeInfo.setEnglishName("zhangsan");
        employeeInfo.setJob("Java");
        employeeInfo.setSex(1);
        employeeInfo.setAge(20);
        employeeInfo.setSalary(new BigDecimal("12500.01"));
        employeeInfo.setJobDay(DATE_FORMAT.parse("2019-09-10"));
        employeeInfo.setRemark("我是备注修改第二次了 ");
        employeeInfoService.saveOrUpdate(employeeInfo);

        EmployeeInfo employeeInfo2 = employeeInfoService.findById(id);
        System.out.println("修改后:");
        System.out.println(JSON.toJSONString(employeeInfo2, SerializerFeature.PrettyFormat));
    }

    /**
     * 删除数据
     */
    @Test
    public void delete() {
        Long id = 1001L;
        employeeInfoService.deleteById(id);
        System.out.println("删除数据结束, id" + id);

        EmployeeInfo employeeInfo1 = employeeInfoService.findById(id);
        System.out.println(JSON.toJSONString(employeeInfo1, SerializerFeature.PrettyFormat));
    }


    /**
     * 根据ID 查询
     */
    @Test
    public void findById() {
        Long id = 1001L;
        EmployeeInfo employeeInfo1 = employeeInfoService.findById(id);
        System.out.println(JSON.toJSONString(employeeInfo1, SerializerFeature.PrettyFormat));
    }
}
