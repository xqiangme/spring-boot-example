package com.example.controller;

import com.example.client.ElasticSearchRestApiClient;
import com.example.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 程序员小强
 * @date 2020-12-14 10:21
 */
@Slf4j
@RestController
@RequestMapping("/elasticSearch")
public class ElasticSearchTestController {

    @Autowired
    private ElasticSearchRestApiClient elasticSearchRestClient;

    private static final String INDEX = "test_index";

    /**
     * 分页查询
     * 使用,from-size 的"浅"分页
     */
    @RequestMapping("searchPageByIndex")
    public Object searchPageByIndex(@RequestParam(value = "pageNo", required = false) Integer pageNo,
				    @RequestParam(value = "pageSize", required = false) Integer pageSize,
				    @RequestParam(value = "index", required = false) String index) {
	pageNo = pageNo == null ? 1 : pageNo;
	pageSize = pageSize == null ? 10 : pageSize;
	index = StringUtils.isEmpty(index) ? INDEX : index;
	List<UserInfo> dataList = elasticSearchRestClient.searchPageByIndex(index, pageNo, pageSize, UserInfo.class);

	Map<String, Object> result = new HashMap<>();
	result.put("index", index);
	result.put("pageNo", pageNo);
	result.put("pageSize", pageSize);
	result.put("dataList", dataList);
	log.info("[ searchPageByIndex ] >> index:{},pageNo:{},pageSize:{}", index, pageNo, pageSize);
	return result;
    }


}
