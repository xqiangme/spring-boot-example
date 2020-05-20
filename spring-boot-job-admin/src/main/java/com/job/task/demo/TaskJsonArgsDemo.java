package com.job.task.demo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 有参数任务-执行Demo
 */
@Slf4j
@Component
public class TaskJsonArgsDemo {

    public void jsonArgsDemo(String json) {
        log.info("[ jsonArgsDemo ] > 任务 start args:{}", json);
        JSONObject jsonObject = JSON.parseObject(json);
        log.info("[ jsonArgsDemo ] > JSON 参数:{}", jsonObject.toString());
        //模拟程序执行
        try {
            Thread.sleep(1200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("[ jsonArgsDemo ] > 任务 end args:{}", json);
    }

}