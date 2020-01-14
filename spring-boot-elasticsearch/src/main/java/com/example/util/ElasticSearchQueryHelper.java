package com.example.util;


import com.alibaba.fastjson.JSON;
import com.example.config.ElasticSearchClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch 查询辅助工具
 *
 * @author mengqiang
 * @version ElasticSearchQueryHelper.java, v 1.0
 */
public class ElasticSearchQueryHelper {

    /**
     * 分页查询
     *
     * @param esClient   客户端
     * @param queryParam 查询参数
     * @param clazz
     */
    public static <T> List<T> searchPage(ElasticSearchClient esClient, ElasticSearchQueryParam queryParam, Class<T> clazz) {
        //转换查询参数
        SearchSourceBuilder searchSourceBuilder = conVerPageSearchBuilder(queryParam);
        //执行查询
        SearchResponse searchResponse = esClient.searchPageByQueryBuilder(queryParam.getIndex(), searchSourceBuilder);
        return buildResultList(searchResponse, clazz);
    }

    /**
     * 计算开始位置
     */
    public static Integer getStartRow(Integer pageNo, Integer pageSize) {
        if (null == pageNo) {
            pageNo = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        return pageSize * (pageNo - 1);
    }


    /**
     * 转换分页查询参数
     */
    private static SearchSourceBuilder conVerPageSearchBuilder(ElasticSearchQueryParam queryParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(getStartRow(queryParam.getPage(), queryParam.getPageSize()));
        searchSourceBuilder.size(queryParam.getPageSize());
        searchSourceBuilder.query(queryParam.getBoolQueryBuilder());

        //单字段排序
        if (!ObjectUtils.isEmpty(queryParam.getSortBuilder())) {
            searchSourceBuilder.sort(queryParam.getSortBuilder());
        }

        //多字段排序
        if (!CollectionUtils.isEmpty(queryParam.getSortBuilderList())) {
            for (FieldSortBuilder sortBuilder : queryParam.getSortBuilderList()) {
                searchSourceBuilder.sort(sortBuilder);
            }
        }
        return searchSourceBuilder;
    }

    /**
     * 构建返回结果
     */
    private static <T> List<T> buildResultList(SearchResponse searchResponse, Class<T> clazz) {
        if (null == clazz) {
            return new ArrayList<>(0);
        }
        if (ObjectUtils.isEmpty(searchResponse) || ObjectUtils.isEmpty(searchResponse.getHits())) {
            return new ArrayList<>(0);
        }
        if (ObjectUtils.isEmpty(searchResponse.getHits().getHits())) {
            return new ArrayList<>(0);
        }

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<T> resultList = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            resultList.add(JSON.parseObject(hit.getSourceAsString(), clazz));
        }

        return resultList;
    }


}