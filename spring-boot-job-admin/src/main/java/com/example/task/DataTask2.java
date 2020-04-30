package com.example.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("dataTask2")
public class DataTask2 {


    public void data2() {
        log.info("【dataTask2】> 任务开始");
    }

}