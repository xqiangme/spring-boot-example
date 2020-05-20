package com.job.admin.web.param.bo;

import lombok.Data;

import java.util.List;

@Data
public class JobTaskUserUpdatePowerBO extends JobBaseOperateBO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 菜单key 集合
     */
    private List<String> menus;

    /**
     * 功能key集
     */
    private List<String> functions;

    private String operateBy;

    private String operateName;

}
