package com.example.controller;

import com.example.enums.SysExceptionEnum;
import com.example.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping("/test1")
    public String test1() {
        return "success test1";
    }


    @RequestMapping("/test2")
    public String test2() {
        if(1==1){
            throw new BusinessException(SysExceptionEnum.USER_ACCESS_DENIED);
        }

        return "success test2";
    }

}
