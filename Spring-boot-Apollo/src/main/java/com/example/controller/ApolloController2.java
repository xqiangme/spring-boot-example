package com.example.controller;

import com.example.config.ApplicationProperty;
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
@RequestMapping("/properties2")
public class ApolloController2 {

    @Autowired
    private ApplicationProperty applicationProperty;

    @GetMapping("/get")
    public Map<String, Object> getProperty() {
        Map<String, Object> map = new HashMap<>();
        map.put("number1", applicationProperty.getNumber1());
        map.put("number2", applicationProperty.getNumber2());
        map.put("str1", applicationProperty.getStr1());
        map.put("str2", applicationProperty.getStr2());
        return map;
    }
}