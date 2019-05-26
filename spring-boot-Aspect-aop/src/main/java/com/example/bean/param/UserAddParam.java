package com.example.bean.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 人员信息新增参数
 *
 * @author 码农猿
 */
@Data
public class UserAddParam implements Serializable {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;
    /**
     * s
     * 真实姓名
     */
    private String realName;

    /**
     * 性别类型
     * <p>
     * 1-男，2-女，3-不详
     */
    private Integer sexType;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * remark测试
     */
    private String remark;


}
