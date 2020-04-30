package com.example.admin.web.param.bo;

import lombok.Data;

@Data
public class JobTaskUpdateStatusBO extends JobBaseOperateBO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 状态
     */
    private Integer status;
}
