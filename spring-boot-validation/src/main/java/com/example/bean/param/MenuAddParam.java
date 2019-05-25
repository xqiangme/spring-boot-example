package com.example.bean.param;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 菜单信息新增参数
 *
 * @author 码农猿
 */
@Data
public class MenuAddParam implements Serializable {

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不为空！")
    private String menuName;

    /**
     * 菜单父id
     */
    @NotBlank(message = "菜单父id不为空！")
    private String menuParentId;

    /**
     * 菜单顺序
     */
    @NotNull(message = "菜单顺序不为空！")
    private Integer menuIndex;

    /**
     * 是否启用
     */
    @NotNull(message = "菜单启用标记不为空！")
    private Boolean enableFlag;

    /**
     * 操作人信息
     */
    @Valid
    @NotNull
    private OperateByParam operateParam;

}
