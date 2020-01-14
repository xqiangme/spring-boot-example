package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.config.ElasticSearchClient;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author mengqiang
 * @version .java, v 0.1   -   -
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Testoo1 {

    @Qualifier("elasticsearchClient")
    @Autowired
    private ElasticSearchClient esClient;

    @Test
    public void exist() {

        String index = "db_test1";
        String type = "user-info";
        String id = "1001";

        boolean existIndex = esClient.exist(index, type, id);
        System.out.println(existIndex);
    }


    @Test
    public void save1() {

        String index = "db_test1";
        String type = "user-info";
        String id = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        Map<String, Object> dataMap = new JSONObject();
        dataMap.put("userId", UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
        dataMap.put("name", "李四2");
//        dataMap.put("age", 25);
        dataMap.put("sex", 15);
        dataMap.put("phone", "13235717261");

//        "sex":[11]
//        "userId" : "72BBDDB1-A54A-43A3-AB22-58AAA4B12BD3"
//
//        72bbddb1-a54a-43a3-ab22-58aaa4b12bd3
        IndexResponse response = esClient.save(index, type, id, dataMap);
        System.out.println("新增返回结果：");
        System.out.println(response);

        GetResponse result = esClient.getById(index, type, id);
        System.out.println("查询当前结果：" + result.isExists());
        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }


    @Test
    public void update() {

        String index = "db_test1";
        String type = "user-info";
        String id = "1003";

        Map<String, Object> dataMap = new JSONObject();
        dataMap.put("userid", "a3003");
        dataMap.put("name", "张三55");
        dataMap.put("age", 1);
        dataMap.put("sex", 1);
        dataMap.put("phone", "13235717261");
        dataMap.put("email", "1234567890");

        UpdateResponse response = esClient.update(index, type, id, dataMap);
        System.out.println("修改返回结果：");
        System.out.println(response);
        System.out.println(response.status());

        GetResponse result = esClient.getById(index, type, id);
        System.out.println("查询当前结果：" + result.isExists());
        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    public void delete() {

        String index = "db_test1";
        String type = "user-info";
        String id = "1001";

        DeleteResponse response = esClient.deleteDataById(index, type, id);
        System.out.println("删除返回结果：");
        System.out.println(response.status().equals(RestStatus.NOT_FOUND));


        GetResponse result = esClient.getById(index, type, id);
        System.out.println("查询当前结果：" + result.isExists());
        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    public void getById() {
        String index = "db_test1";
        String type = "user-info";
        String id = "1001";
        GetResponse result = esClient.getById(index, type, id);
        System.out.println();
        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }

    @Test
    public void getByIdList() throws Exception {
        String index = "db_test1";
        String type = "user-info";
        List<String> idList = new ArrayList<>();
        idList.add("9lM1J28BYv0Sqv_8_oCG");
//        idList.add("9lM1J28BYv0Sqv_8_oCG");
        esClient.getByIdList(index, type, idList);
        System.out.println();
//        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }


    @Test
    public void batchSave() {
        String index = "db_test1";
        String type = "user-info2";

        Map<String, Object> dataMap = null;
        List<Object> dataList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            dataMap = new JSONObject();
            String userId = "Z" + (3000 + i);
            dataMap.put("userId", userId);
            dataMap.put("name", "李四" + i);
            dataMap.put("age", 20);
            dataMap.put("sex", 1 + i);
            dataMap.put("phone", "13235717261");
            dataMap.put("email", "123" + i);
            dataList.add(dataMap);
        }

        BulkResponse result = esClient.batchSaveOrUpdateData(index, type, dataList);
        System.out.println(result);
        System.out.println();
    }


    @Test
    public void getAllIndex() {
        String index = "db_test1";
        String type = "user-info";

        SearchResponse result = esClient.getAllIndex(index, type);
        System.out.println(result);

        System.out.println();
        SearchHit[] hits = result.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(JSON.toJSONString(hit.getSourceAsMap(), SerializerFeature.PrettyFormat));
//            boolean deleteById = esClient.deleteById(index, type, hit.getId());
//            System.out.println(deleteById);
        }
    }

    @Test
    public void searchByQueryBuilderPage() {
        String index = "db_test1";
        String type = "user-info";

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(QueryBuilders.("email", "1234567890"));

        new MatchAllQueryBuilder();
        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("userId", "a3ad6c7bbab644adb37b11be953d3b43");
        boolQueryBuilder.must(termsQueryBuilder);
//        boolQueryBuilder.must(new WildcardQueryBuilder("name", "张三3"));
        Integer pageFrom = 0;
        Integer pageSize = 50;
        String sortName = "age";
        SearchResponse result = esClient.searchPageByQueryBuilder(index, pageFrom, pageSize, boolQueryBuilder, sortName, SortOrder.DESC);
        System.out.println(result);

        System.out.println();
        SearchHit[] hits = result.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(JSON.toJSONString(hit.getSourceAsMap(), SerializerFeature.PrettyFormat));
//            boolean deleteById = esClient.deleteById(index, type, hit.getId());
//            System.out.println(deleteById);
        }
    }

}