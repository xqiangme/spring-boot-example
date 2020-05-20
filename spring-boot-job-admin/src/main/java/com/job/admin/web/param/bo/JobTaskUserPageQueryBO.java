package com.job.admin.web.param.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-04-14 00:00
 */
@Data
public class JobTaskUserPageQueryBO implements Serializable {

    private static final long serialVersionUID = 4443152381870746507L;

    private Integer page;
    private Integer limit;

    private Integer userStatus;
    private Integer userType;

    private String usernameLike;

    private String realNameLike;

}
