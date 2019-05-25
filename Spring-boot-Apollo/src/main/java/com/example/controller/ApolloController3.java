package com.example.controller;

import com.example.config.TwoProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 码农猿
 * @version .java, v 0.1   -   -
 */
@RestController
@RequestMapping("/properties3")
public class ApolloController3 {

    @Autowired
    private TwoProperty twoProperty;

    @GetMapping("/get")
    public Map<String, Object> getProperty() {
        Map<String, Object> map = new HashMap<>();
        map.put("number1", twoProperty.getNumber1());
        map.put("number2", twoProperty.getNumber2());
        map.put("str2", twoProperty.getStr2());
        map.put("str1", twoProperty.getStr1());
        return map;
    }
}