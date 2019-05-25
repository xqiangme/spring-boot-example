package com.example;

import com.alibaba.fastjson.JSON;
import com.example.bean.param.UserAddParam;
import com.example.bean.result.ValidateResult;
import com.example.util.ValidateUtil;

/**
 * 自定义工具-执行校验
 *
 * @author 码农猿
 */
public class ValidateUtilTest {
    public static void main(String[] args) {
        UserAddParam userAddParam = new UserAddParam();
        userAddParam.setMobile("123");

        ValidateResult validate = ValidateUtil.validate(userAddParam);
        //输出结果
        System.out.println("校验结果 >> " + JSON.toJSONString(validate));
    }
}