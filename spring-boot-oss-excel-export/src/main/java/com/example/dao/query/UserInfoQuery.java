package com.example.dao.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询参数
 *
 * @author 程序员小强
 * @date 2020-11-06 20:59
 */
@Data
public class UserInfoQuery implements Serializable {

    private static final long serialVersionUID = 8991792994699088437L;

    private String mobile;

    private Integer startRow;

    private Integer pageSize;

}
