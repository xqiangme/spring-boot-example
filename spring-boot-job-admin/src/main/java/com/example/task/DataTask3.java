package com.example.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("dataTask3")
public class DataTask3 {


    public void data3(String param) {
        log.info("【dataTask3】> 任务开始 :{}", param);
    }

}