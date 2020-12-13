package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.Application;
import com.example.client.ElasticSearchRestApiClient;
import com.example.model.UserInfo;
import com.example.util.PageUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ElasticSearch 查询测试
 *
 * @author 程序员小强
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ElasticSearchQueryTest {

    @Autowired
    private ElasticSearchRestApiClient elasticSearchRestClient;

    private static final String INDEX = "test_index";

    /**
     * 分页查询
     * 使用,from-size 的"浅"分页
     */
    @Test
    public void searchPageByIndex() {
        int pageNo = 2;
        int pageSize = 2;
        List<UserInfo> dataList = elasticSearchRestClient.searchPageByIndex(INDEX, pageNo, pageSize, UserInfo.class);

        System.out.println(String.format("分页查询, pageNo:%d , pageSize:%d", pageNo, pageSize));
        System.out.println(JSON.toJSONString(dataList, SerializerFeature.PrettyFormat));

    }


    /**
     * 聚合条件查询
     */
    @Test
    public void searchByQuery() {
        int pageNo = 1;
        int pageSize = 10;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页参数
        searchSourceBuilder.from(PageUtils.getStartRow(pageNo, pageSize));
        searchSourceBuilder.size(pageSize);

        // bool 查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        //排序
        searchSourceBuilder.sort("age");

        List<UserInfo> dataList = elasticSearchRestClient.searchByQuery(INDEX, searchSourceBuilder, UserInfo.class);

        System.out.println(String.format("聚合条件查询, searchSourceBuilder:%s", searchSourceBuilder));
        System.out.println(String.format("集合大小, dataListSize:%d", dataList.size()));
        System.out.println(JSON.toJSONString(dataList, SerializerFeature.PrettyFormat));
    }
}
