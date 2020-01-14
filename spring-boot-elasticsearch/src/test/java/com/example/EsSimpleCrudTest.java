package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.config.ElasticSearchClient;
import com.example.model.UserInfo;
import org.elasticsearch.action.bulk.BulkResponse;
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
public class EsSimpleCrudTest {

    @Autowired
    @Qualifier("elasticSearchClient")
    private ElasticSearchClient esClient;

    private static final String INDEX = "db_test03";
    private static final String TYPE = "user_info";

    @Test
    public void exist() {
        String id = "1001";
        boolean existIndex = esClient.exist(INDEX, TYPE, id);
        System.out.println(existIndex);
    }


    @Test
    public void save1() {
        String userId = getUuId();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserName("zhangsan");
        userInfo.setRealName("李十四");
        userInfo.setMobile("15725353777");
        userInfo.setAge(20);
        userInfo.setProgressRate(new BigDecimal("0.09"));
        userInfo.setCreateTime(new Date());
        userInfo.setRemarks(null);
        esClient.save(INDEX, TYPE, userId, userInfo);

        UserInfo userInfo1 = esClient.getById(INDEX, TYPE, userId, UserInfo.class);
        System.out.println("查询结果");
        System.out.println(JSON.toJSONString(userInfo1, SerializerFeature.PrettyFormat));
    }

    @Test
    public void getByIdList() {
        List<String> idList = new ArrayList<>();
        idList.add("90531731a39a43d3ba8c0bf937367ee0");
        idList.add("6b50d534ce7c4f86821cc8466aa8a5c2");
        List<UserInfo> userInfoList = esClient.getByIdList(INDEX, TYPE, idList, UserInfo.class);
        System.out.println("查询结果");
        System.out.println(JSON.toJSONString(userInfoList, SerializerFeature.PrettyFormat));
    }


    @Test
    public void batchSaveOrUpdate() {
        List<String> idList = new ArrayList<>();
        idList.add("90531731a39a43d3ba8c0bf937367ee0");
        idList.add("6b50d534ce7c4f86821cc8466aa8a5c2");
        List<UserInfo> userInfoList = esClient.getByIdList(INDEX, TYPE, idList, UserInfo.class);

        for (UserInfo userInfo : userInfoList) {
            userInfo.setRealName(userInfo.getRealName() + "03");
            userInfo.setUserName(userInfo.getUserName() + "03");
            userInfo.setUpdateTime(new Date());
            userInfo.setProgressRate(new BigDecimal("0.035"));
        }

        BulkResponse responses = esClient.batchSaveOrUpdateData(INDEX, TYPE, userInfoList);
        System.out.println("结果");
        System.out.println(JSON.toJSONString(responses, SerializerFeature.PrettyFormat));
    }


    @Test
    public void batchSaveAsync() throws Exception {
        List<UserInfo> userInfoList = new ArrayList<>();
        UserInfo userInfo = null;
        for (int i = 1; i <= 10; i++) {
            userInfo = new UserInfo();
            userInfo.setUserId(getUuId());
            userInfo.setUserName("async" + i);
            userInfo.setRealName("异步" + i);
            userInfo.setMobile("15725353777");
            userInfo.setAge(20 + i);
            userInfo.setProgressRate(new BigDecimal("0.09"));
            userInfo.setCreateTime(new Date());
            userInfo.setRemarks(null);
            userInfoList.add(userInfo);
        }

        esClient.batchSaveOrUpdateAsync(INDEX, TYPE, userInfoList);
        Thread.sleep(2000);
        System.out.println("执行完毕 size=" + userInfoList.size());
    }


    public String getUuId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}