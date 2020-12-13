package com.example.dao.query;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询参数
 *
 * @author 程序员小强
 * @date 2020-11-06 20:59
 */
@Data
public class StudentInfoQuery implements Serializable {

    private static final long serialVersionUID = 4178928573840181745L;

    private String classId;

    private String name;

    private Integer sex;

    private Date createStartTime;

    private Date createEndTime;

    private Integer startRow;

    private Integer pageSize;

}
