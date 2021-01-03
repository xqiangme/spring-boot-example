package com.example.elasticsearch.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * 注解@Document 指定实体类的索引和类型。默认所有的属性都是索引的
 * 1、indexName ：　指定索引
 * 2、shards：指定分片的数量
 * 3、replicas：指定副本的数量
 */
@Data
@Document(indexName = "User", shards = 2, replicas = 2)
public class UserInfo {

    @Id
    private Integer userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", fielddata = true, store = false)
    private String userName;

    private String password;


    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

    private List<String> hobbies;
}
