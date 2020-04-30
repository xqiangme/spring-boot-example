package com.example.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("dataTask1")
public class DataTask1 {


    public void data1() {
        log.info("【dataTask1】> 任务开始");
    }

}