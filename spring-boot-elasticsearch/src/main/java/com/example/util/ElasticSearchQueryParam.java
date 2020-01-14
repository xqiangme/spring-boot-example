package com.example.util;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.util.List;

/**
 * @author mengqiang
 */
public class ElasticSearchQueryParam {

    /**
     * Elasticsearch-索引
     */
    private String index;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 页容量
     */
    private Integer pageSize;

    /**
     * 查询参数
     */
    private BoolQueryBuilder boolQueryBuilder;

    /**
     * 排序参数
     */
    private FieldSortBuilder sortBuilder;

    /**
     * 多个排序排序参数
     */
    private List<FieldSortBuilder> sortBuilderList;

    public ElasticSearchQueryParam(String index) {
        this.index = index;
    }

    public ElasticSearchQueryParam setIndex(String index) {
        this.index = index;
        return this;
    }

    public ElasticSearchQueryParam setPage(Integer page) {
        this.page = page;
        return this;
    }

    public ElasticSearchQueryParam setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ElasticSearchQueryParam setBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder) {
        this.boolQueryBuilder = boolQueryBuilder;
        return this;
    }

    public ElasticSearchQueryParam setSortBuilder(FieldSortBuilder sortBuilder) {
        this.sortBuilder = sortBuilder;
        return this;
    }

    public ElasticSearchQueryParam setSortBuilderList(List<FieldSortBuilder> sortBuilderList) {
        this.sortBuilderList = sortBuilderList;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public BoolQueryBuilder getBoolQueryBuilder() {
        return boolQueryBuilder;
    }

    public FieldSortBuilder getSortBuilder() {
        return sortBuilder;
    }

    public List<FieldSortBuilder> getSortBuilderList() {
        return sortBuilderList;
    }
}