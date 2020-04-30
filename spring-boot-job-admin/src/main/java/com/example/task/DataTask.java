package com.example.task;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component("dataTask")
public class DataTask {


    public void data() {
        log.info("【dataTask】> 任务开始");
    }

}