package com.example.web.bo;

import lombok.Data;

/**
 * 人员新增参数
 *
 * @author mengq
 * @date 2020-07-26
 */
@Data
public class UserAddBO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * remark
     */
    private String remark;
}