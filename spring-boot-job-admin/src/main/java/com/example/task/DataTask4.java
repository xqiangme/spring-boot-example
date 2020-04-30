package com.example.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("dataTask4")
public class DataTask4 {


    public void data4(String param1, String param2) {
        log.info("【dataTask4】> 任务开始 param:{},param2:{}", param1, param2);
    }

}