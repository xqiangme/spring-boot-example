package com.job.admin.web.param.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfoPageVO implements Serializable {

    private static final long serialVersionUID = -8300350474675960784L;

    private Integer total;

    private List<UserInfoPageDetailVO> list;

    public static UserInfoPageVO initDefault() {
        UserInfoPageVO result = new UserInfoPageVO();
        result.setTotal(0);
        result.setList(new ArrayList<>(0));
        return result;
    }

    public UserInfoPageVO() {
    }

    public UserInfoPageVO(Integer total, List<UserInfoPageDetailVO> list) {
        this.total = total;
        this.list = list;
    }

}
