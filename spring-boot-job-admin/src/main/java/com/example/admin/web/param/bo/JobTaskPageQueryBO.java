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
 * @date 2020-04-14 00:00
 * @desc
 */
@Data
public class JobTaskPageQueryBO implements Serializable {
    private static final long serialVersionUID = 3234394229041271191L;

    private Integer page;

    private Integer limit;

    private String jobNameLike;

    private String jobMethodLike;

    private Integer jobStatus;

}
