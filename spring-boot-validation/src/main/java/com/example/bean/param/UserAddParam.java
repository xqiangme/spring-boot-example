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
    @NotBlank(message = "用户名不为空！")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不为空！")
    @Size(min = 6, max = 20, message = "密码长度介于{min}至{max}之间！")
    private String userPassword;
    /**
     * s
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不为空！")
    private String realName;

    /**
     * 性别类型
     * <p>
     * 1-男，2-女，3-不详
     */
    @NotNull
    private Integer sexType;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不为空！")
    @Length(max = 11)
    private String mobile;

    /**
     * remark测试
     */
    private String remark;

    //省略 get set

}
