package com.example.bean.param;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


/**
 * 操作人参数
 *
 * @author 码农猿
 */
@Data
public class OperateByParam {

    /**
     * 操作人id
     */
    @NotBlank(message = "操作人id不为空！")
    private String operateBy;


    /**
     * 操作人名称
     */
    @NotBlank(message = "操作人名称不为空！")
    private String operateName;


}
