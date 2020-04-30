package com.example.admin.dao.dto;

import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author mengq
 */
@Builder
@ToString
public class JobUserPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 7120941057339818885L;
    
    private Integer limit;
    private Integer pageSize;
    private String projectKey;

    private Integer userStatus;
    private Integer adminFlag;
    private String usernameLike;
    private String realNameLike;

}
