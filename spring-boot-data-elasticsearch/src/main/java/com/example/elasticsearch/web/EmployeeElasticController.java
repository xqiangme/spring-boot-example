package com.example.elasticsearch.web;

import com.example.elasticsearch.bean.EmployeeInfo;
import com.example.elasticsearch.service.EmployeeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 员工数据-ES接口测试
 *
 * @author 程序员小强
 * @date 2020-12-08
 */
@Slf4j
@RestController
@RequestMapping("/employeeInfo")
public class EmployeeElasticController {

    @Autowired
    private EmployeeInfoService employeeInfoService;

    @RequestMapping("/batchSave")
    public String init() throws Exception {
        List<EmployeeInfo> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        list.add(new EmployeeInfo(1001L, "2001", "张三", "zhangsan", "Java", 1, 19, new BigDecimal("12500.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1002L, "2002", "李四", "lisi", "PHP", 1, 18, new BigDecimal("11600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1003L, "2003", "王五", "wangwu", "C++", 1, 20, new BigDecimal("9900.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1004L, "2004", "赵六", "zhaoliu", "Java Leader", 1, 20, new BigDecimal("20000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1005L, "2005", "小五", "xiaowu", "H5", 1, 17, new BigDecimal("10600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1006L, "2006", "小六", "xaioliu", "web", 1, 20, new BigDecimal("12600.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1007L, "2007", "小七", "xiaoqi", "app", 1, 22, new BigDecimal("20000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1008L, "2008", "小八", "xaioba", "Java", 1, 21, new BigDecimal("11000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1009L, "2009", "小九", "xiaojiu", "Java", 1, 20, new BigDecimal("14000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        list.add(new EmployeeInfo(1010L, "2010", "大十", "dashi", "Java", 1, 20, new BigDecimal("13000.01"), simpleDateFormat.parse("2019-09-10"), "备注"));
        employeeInfoService.batchSaveOrUpdate(list);
        return "success -> " + list.size();
    }

    @GetMapping("/listAll")
    public Iterator<EmployeeInfo> all() {
        return employeeInfoService.findAll();
    }

}
