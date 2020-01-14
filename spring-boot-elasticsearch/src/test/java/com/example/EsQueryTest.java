package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.config.ElasticSearchClient;
import com.example.model.UserInfo;
import com.example.util.ElasticSearchQueryHelper;
import com.example.util.ElasticSearchQueryParam;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author mengqiang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EsQueryTest {

    @Autowired
    @Qualifier("elasticSearchClient")
    private ElasticSearchClient esClient;

    private static final String INDEX = "db_test03";
    private static final String TYPE = "user_info";

    @Test
    public void batchSave() throws Exception {
        List<UserInfo> userInfoList = new ArrayList<>();
        UserInfo userInfo = null;
        for (int i = 1; i <= 10000; i++) {
            userInfo = new UserInfo();
            userInfo.setUserId(getUuId());
            userInfo.setUserName("async_" + i);
            userInfo.setRealName("异步_" + i);
            userInfo.setMobile("15725353777");
            userInfo.setAge(20 + i);
            userInfo.setProgressRate(new BigDecimal("0.09"));
            userInfo.setCreateTime(new Date());
            userInfo.setRemarks(null);
            userInfoList.add(userInfo);
        }

//        System.out.println("查询结果");
//        System.out.println(JSON.toJSONString(userInfoList, SerializerFeature.PrettyFormat));

        BulkResponse responses = esClient.batchSaveOrUpdateData(INDEX, TYPE, userInfoList);

    }


    @Test
    public void searchByQueryBuilderPage() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        Integer pageFrom = 0;
        Integer pageSize = 10;
        String sortName = "age";
        SearchResponse result = esClient.searchPageByQueryBuilder(INDEX, pageFrom, pageSize, boolQueryBuilder, sortName, SortOrder.DESC);

        System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
        String scrollId = result.getScrollId();
        System.out.println("scrollId=" + scrollId);
        SearchHit[] hits = result.getHits().getHits();

//        for (SearchHit hit : hits) {
//            System.out.println(JSON.toJSONString(hit.getSourceAsMap(), SerializerFeature.PrettyFormat));
//        }

//        System.out.println(hits.length);
    }


    @Test
    public void searchPage() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilder qb= QueryBuilders.termQuery("real_name","张十");
        boolQueryBuilder.filter(qb);
        ElasticSearchQueryParam param = new ElasticSearchQueryParam(INDEX)
                .setPage(1)
                .setPageSize(10)
                .setBoolQueryBuilder(boolQueryBuilder)
                .setSortBuilder(new FieldSortBuilder("age").order(SortOrder.ASC));

        List<UserInfo> userInfoList = ElasticSearchQueryHelper.searchPage(esClient, param, UserInfo.class);
        System.out.println(JSON.toJSONString(userInfoList, SerializerFeature.PrettyFormat));
    }


    public String getUuId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}