package com.example.model;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 人员信息实体类
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @JSONField(name = "id")
    private String userId;

    /**
     * 用户名
     */
    @JSONField(name = "user_name")
    private String userName;

    /**
     * 真实姓名
     */
    @JSONField(name = "real_name")
    private String realName;

    /**
     * 手机号
     */
    @JSONField(name = "mobile")
    private String mobile;

    /**
     * 年龄
     */
    @JSONField(name = "age")
    private Integer age;

    /**
     * 进步率
     * 注：小数测试
     */
    @JSONField(name = "progress_rate")
    private BigDecimal progressRate;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    private String remarks;
}
