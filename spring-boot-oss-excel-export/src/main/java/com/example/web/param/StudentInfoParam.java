package com.example.web.param;

import com.example.common.BasePageModel;
import lombok.Data;

/**
 * 学生查询参数
 *
 * @author 程序员小强
 * @date 2020-11-06 20:59
 */
@Data
public class StudentInfoParam extends BasePageModel {

    private static final long serialVersionUID = 5798276314373500498L;

    private String classId;

    private String name;

    private Integer sex;

    private String createStartTime;

    private String createEndTime;


}
