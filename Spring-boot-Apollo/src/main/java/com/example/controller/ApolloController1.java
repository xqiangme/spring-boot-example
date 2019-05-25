package com.example.controller;

import com.example.config.ApplicationKeyProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/properties1")
public class ApolloController1 {

    @Autowired
    private ApplicationKeyProperty keyProperty;

    @Value("${apollo.key.demo}")
    private String demo;

    @Value("${server.port}")
    private String port;

    @Value("${apollo.bootstrap.namespaces :'application'}")
    private String namespaces;

    @GetMapping("/get")
    public Map<String, Object> getProperty() {
        Map<String, Object> map = new HashMap<>();
        map.put("demoKey1", keyProperty.getDemoKey1());
        map.put("demoKey2", keyProperty.getDemoKey2());
        map.put("port", port);
        map.put("namespaces", namespaces);
        return map;
    }

}