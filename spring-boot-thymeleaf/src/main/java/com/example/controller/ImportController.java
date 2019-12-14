
package com.example.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
public class ImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);


    /**
     * 导入
     */
    @PostMapping("/importList")
    public Object importList(@RequestParam(value = "file", required = true) MultipartFile file) {

        long time = System.currentTimeMillis();

        LOGGER.info("[{}] >> 设备黑名单-黑名单导入", time);
        LOGGER.info("[{}] >> 文件是否为空！{}", time, file.isEmpty());
        LOGGER.info("[{}] >> 文件名: {}", time, file.getOriginalFilename());
        LOGGER.info("");


        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", "");
        map.put("errorCode", "");
        map.put("errorMsg", "");
        return map;
    }


}