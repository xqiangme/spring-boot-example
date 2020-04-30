/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.example.admin.web.param.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-14 00:44
 * @desc
 */
@Data
public class JobBaseOperateBO implements Serializable {

    private static final long serialVersionUID = -4793987603724560341L;
    /**
     * 操作人ID
     */
    private String operateBy;

    /**
     * 操作人名称
     */
    private String operateName;

}
