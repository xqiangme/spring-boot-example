package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.Application;
import com.example.client.ElasticSearchDocModel;
import com.example.client.ElasticSearchRestApiClient;
import com.example.model.UserInfo;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * ElasticSearch 文档-增删改测试
 *
 * @author 程序员小强
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ElasticSearchCrudTest {

    @Autowired
    private ElasticSearchRestApiClient elasticSearchIndexClient;

    private static final String INDEX = "test_index";

    /**
     * 判断索引是否存在
     */
    @Test
    public void existsIndex() {
        String index = "test_index";
        boolean exists = elasticSearchIndexClient.existsIndex(index);
        System.out.println(String.format("判断索引是否存在, index:%s , 是否存在:%b", index, exists));
    }

    /**
     * 判断文档是否存在
     */
    @Test
    public void existsDocument() {
        String index = "test_index";
        String id = "9de70a8227c3420bbe6f22b5135b8123";
        boolean exists = elasticSearchIndexClient.existsDocument(index, id);
        System.out.println(String.format("判断文档是否存在, index:%s , id:%s, 是否存在:%b", index, id, exists));
    }

    /**
     * 保存数据-自定义数据ID
     */
    @Test
    public void save() {
        String userId = getUuId();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserName("lisi2");
        userInfo.setRealName("李四2");
        userInfo.setMobile("13725353777");
        userInfo.setAge(21);
        userInfo.setGrades(new BigDecimal("550.09"));
        userInfo.setCreateTime(new Date());
        IndexResponse response = elasticSearchIndexClient.save(INDEX, userId, userInfo);
        System.out.println(String.format("数据保存完毕, id:%s", response.getId()));

        UserInfo userInfo1 = elasticSearchIndexClient.getById(INDEX, userId, UserInfo.class);
        System.out.println(String.format("查询结果:%s", response.getId()));
        System.out.println(JSON.toJSONString(userInfo1, SerializerFeature.PrettyFormat));
    }

    /**
     * 批量保存数据-自定义数据ID
     */
    @Test
    public void batchSave() {
        List<ElasticSearchDocModel<?>> documentList = new ArrayList<>();
        UserInfo userInfo = null;
        for (int i = 0; i <= 5; i++) {
            String userId = getUuId();
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setUserName("zhangsan");
            userInfo.setRealName("我是" + i);
            userInfo.setMobile("15725353777");
            userInfo.setAge(20);
            userInfo.setGrades(new BigDecimal("690.09"));
            userInfo.setCreateTime(new Date());
            documentList.add(new ElasticSearchDocModel<>(userId, userInfo));
        }

        elasticSearchIndexClient.batchSaveOrUpdate(INDEX, documentList);
        System.out.println("批量保存数据结束");
    }

    /**
     * 根据ID修改
     */
    @Test
    public void updateById() {
        String userId = "9de70a8227c3420bbe6f22b5135b8123";
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserName("zhangsan2");
        userInfo.setRealName("张三2");
        userInfo.setMobile("15725353777");
        userInfo.setAge(22);
        userInfo.setGrades(new BigDecimal("580.09"));
        userInfo.setCreateTime(new Date());

        UpdateResponse updateResponse = elasticSearchIndexClient.updateById(INDEX, userId, userInfo);
        System.out.println(String.format("修改的数据 ID:%s, 版本:%d", updateResponse.getId(), updateResponse.getVersion()));

    }

    /**
     * 根据ID部分修改
     */
    @Test
    public void updateByIdSelective() {
        String userId = "100008100927";
        MySkuInfo skuInfo = new MySkuInfo();
        skuInfo.setAppraiseNum(3500);
        skuInfo.setGoodsName("小米10 至尊纪念版");

        UpdateResponse updateResponse = elasticSearchIndexClient.updateByIdSelective("sku_test3", userId, skuInfo);
        System.out.println(String.format("修改的数据 ID:%s, 版本:%d", updateResponse.getId(), updateResponse.getVersion()));

    }

    /**
     * 根据id集批量获取数据
     */
    @Test
    public void getByIdList() {
        List<String> idList = new ArrayList<>();
        idList.add("9de70a8227c3420bbe6f22b5135b8123");
        idList.add("d707b8637a724ff09b9c1591d8a33576");
        List<UserInfo> userInfoList = elasticSearchIndexClient.getByIdList(INDEX, idList, UserInfo.class);
        System.out.println("批量查询结果");
        System.out.println(JSON.toJSONString(userInfoList, SerializerFeature.PrettyFormat));
    }


    /**
     * 根据ID删除
     */
    @Test
    public void deleteById() {
        String userId = "d707b8637a724ff09b9c1591d8a33576";
        boolean deleteFlag = elasticSearchIndexClient.deleteById(INDEX, userId);
        System.out.println(String.format("判断索引是否存在, index:%s , 是否存在:%b", userId, deleteFlag));
    }

    /**
     * 根据文档 ID 批量删除文档
     */
    @Test
    public void deleteByIdList() {
        List<String> idList = new ArrayList<>();
        idList.add("b824025525004e048a34c2398931183d");
        idList.add("5053444f587145e5af5d866245653a5e");
        elasticSearchIndexClient.deleteByIdList(INDEX, idList);
        System.out.println("批量删除文档完成");
    }

    public String getUuId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
