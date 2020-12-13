package com.example.elasticsearch.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工实体类
 *
 * @author 程序员小强
 * @date 2020-12-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "employee_info", shards = 2, replicas = 2)
public class EmployeeInfo {

    @Id
    private Long id;

    /**
     * 工号
     */
    @Field(name = "job_no")
    private String jobNo;

    /**
     * 姓名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", fielddata = true)
    private String name;

    /**
     * 英文名
     */
    @Field(name = "english_name")
    private String englishName;

    /**
     * 工作岗位
     */
    private String job;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 薪资
     */
    private BigDecimal salary;

    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(name = "job_day", format = DateFormat.date_time)
    private Date jobDay;

    /**
     * 备注
     */
    private String remark;

}
