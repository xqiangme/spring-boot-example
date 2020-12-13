package com.example.web.param;

import com.example.common.BasePageModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoParam extends BasePageModel {

    private String mobile;

}