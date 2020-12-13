package com.example.demo;

import com.example.Application;
import com.example.client.ElasticSearchRestApiClient;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * ElasticSearch 索引相关测试
 *
 * @author 程序员小强
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ElasticSearchIndexTest {

    @Autowired
    private ElasticSearchRestApiClient elasticSearchIndexClient;

    /**
     * 判断索引是否存在
     */
    @Test
    public void existsIndex() {
        String index = "test_index2";
        boolean exists = elasticSearchIndexClient.existsIndex(index);
        System.out.println(String.format("判断索引是否存在, index:%s , 是否存在:%b", index, exists));
    }

    /**
     * 创建 ES 索引
     */
    @Test
    public void createIndex() {
        String index = "test_index2";
        //文档属性Mapping集
        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");

        Map<String, Object> value = new HashMap<>();
        value.put("type", "keyword");

        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        properties.put("value", value);

        boolean createIndexFlag = elasticSearchIndexClient.createIndex(index, properties);
        System.out.println(String.format("创建索引, index:%s , createIndexFlag:%b", index, createIndexFlag));
    }

    /**
     * 获取索引配置
     */
    @Test
    public void getIndexSetting() {
        String index = "test_index2";
        GetSettingsResponse response = elasticSearchIndexClient.getIndexSetting(index);
        System.out.println(String.format("获取索引配置, index:%s", index));
        System.out.println(response);
    }


    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        String index = "test_index2";
        boolean deleteIndexFlag = elasticSearchIndexClient.deleteIndex(index);
        System.out.println(String.format("删除索引, index:%s , deleteIndexFlag:%b", index, deleteIndexFlag));
    }
}
