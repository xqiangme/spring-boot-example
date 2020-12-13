package com.example.client;

/**
 * ElasticSearch 批量操作公共model
 *
 * @author 程序员小强
 */
public class ElasticSearchDocModel<T> {

    /**
     * 文档ID
     * <p>
     */
    private String id;

    /**
     * 文档内容
     */
    private T data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ElasticSearchDocModel() {
    }

    public ElasticSearchDocModel(String id, T data) {
        this.id = id;
        this.data = data;
    }
}
