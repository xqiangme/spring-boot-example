package com.example.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.exception.ElasticsearchRunException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch 连接客户端
 * <p>
 * 注：
 * index-类似关系型数据库
 * type-类似关系型数据表
 *
 * @author mengqiang
 * @date 2019-12-21
 */
public class ElasticSearchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);

    private String host;
    private int port;
    private String scheme;

    private RestHighLevelClient client;

    public ElasticSearchClient() {
    }

    public ElasticSearchClient(String host, int port, String scheme) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }

    /**
     * 初始化连接
     */
    public void init() {
        if (null != client) {
            LOGGER.warn("[ ElasticSearch-Client ] >> 重复连接, host = {} , port = {}", host, port);
        }
        RestClientBuilder restClientBuilder = RestClient
                .builder(new HttpHost(host, port, scheme))
                //超时时间10s
                .setMaxRetryTimeoutMillis(10000);
        client = new RestHighLevelClient(restClientBuilder);
        LOGGER.info("[ ElasticSearch-Client ] >>  init success , host = {} , port = {}", host, port);
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            LOGGER.info("[ ElasticSearch-Client ] >> Closing elasticSearch client start");
            if (null != client) {
                client.close();
            }
            LOGGER.info("[ ElasticSearch-Client ] >> Closing elasticSearch client end ");
        } catch (final Exception e) {
            LOGGER.error("[ ElasticSearch-Client ] >> Error closing ElasticSearch client: ", e);
        }
    }

    /**
     * 判断数据是否存在
     *
     * @param index 索引
     * @param type  类型
     * @param id    数据ID
     * @return 是否存在
     */
    public boolean exist(String index, String type, String id) {
        try {
            GetRequest getRequest = new GetRequest(index, type, id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            getRequest.storedFields("_none_");
            return client.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 判断数据是否存在异常,index = {},stack={}", index, e);
        }
        return false;
    }

    /**
     * 保存数据-随机生成数据ID
     *
     * @param index     索引
     * @param type      类型
     * @param dataValue 数据内容
     */
    public IndexResponse save(String index, String type, Object dataValue) {
        try {
            IndexRequest request = new IndexRequest(index, type);
            request.source(JSON.toJSONString(dataValue), XContentType.JSON);
            //同步保存数据到es
            return client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 保存数据异常,index = {},type = {},dataValue={} ,stack={}",
                    index, type, dataValue, e);
            throw new ElasticsearchRunException("保存数据到 ElasticSearch 异常");
        }
    }

    /**
     * 保存数据-自定义数据ID
     *
     * @param index     索引
     * @param type      类型
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public IndexResponse save(String index, String type, String id, Object dataValue) {
        try {
            IndexRequest request = new IndexRequest(index, type, id);
            request.source(JSON.toJSONString(dataValue), XContentType.JSON);
            return client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 保存数据异常,index = {},type = {},id = {},dataValue={} ,stack={}",
                    index, type, id, dataValue, e);

            throw new ElasticsearchRunException("保存数据到 ElasticSearch 异常");
        }
    }

    /**
     * 修改
     *
     * @param index     索引
     * @param type      类型
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public UpdateResponse update(String index, String type, String id, Object dataValue) {
        try {
            UpdateRequest request = new UpdateRequest(index, type, id)
                    .doc(JSON.toJSONString(dataValue), XContentType.JSON);
            return client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 修改数据异常,index = {},type = {},id = {},dataValue={} ,stack={}",
                    index, type, id, dataValue, e);
            throw new ElasticsearchRunException("修改 ElasticSearch 数据异常");
        }
    }

    /**
     * 部分修改()
     * 注：1).可变更已有字段值，可新增字段，删除字段无效
     * 2).若当前ID数据不存在则新增
     *
     * @param index     索引
     * @param type      类型
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public UpdateResponse updateSelective(String index, String type, String id, Object dataValue) {
        try {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(dataValue));
            UpdateRequest request = new UpdateRequest(index, type, id)
                    .doc(jsonObject)
                    .upsert(jsonObject);
            return client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 动态修改数据异常,index = {},type = {},id = {},dataValue={} ,stack={}",
                    index, type, id, dataValue, e);
            throw new ElasticsearchRunException("修改 ElasticSearch 数据异常");
        }
    }

    /**
     * 根据id查询
     *
     * @param index 索引
     * @param type  类型
     * @param id    数据ID
     * @return T
     */
    public <T> T getById(String index, String type, String id, Class<T> clazz) {
        GetResponse getResponse = getById(index, type, id);
        return JSON.parseObject(getResponse.getSourceAsString(), clazz);
    }

    /**
     * 根据id查询
     *
     * @param index 索引
     * @param type  类型
     * @param id    数据ID
     * @return GetResponse
     */
    public GetResponse getById(String index, String type, String id) {
        try {
            GetRequest getRequest = new GetRequest(index, type, id);
            return client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 根据id查询异常,index = {},type = {},id = {},stack={}",
                    index, type, id, e);
            throw new ElasticsearchRunException("ElasticSearch 查询异常");
        }
    }

    /**
     * 根据id集批量获取数据
     *
     * @param index  索引
     * @param type   类型
     * @param idList 数据ID集
     * @return T
     */
    public <T> List<T> getByIdList(String index, String type, List<String> idList, Class<T> clazz) {
        MultiGetItemResponse[] responses = getByIdList(index, type, idList);
        if (null == responses || responses.length == 0) {
            return new ArrayList<>(0);
        }

        List<T> resultList = new ArrayList<>(responses.length);
        for (MultiGetItemResponse response : responses) {
            GetResponse getResponse = response.getResponse();
            if (!getResponse.isExists()) {
                continue;
            }
            resultList.add(JSON.parseObject(getResponse.getSourceAsString(), clazz));
        }

        return resultList;
    }


    /**
     * 根据id集批量获取数据
     *
     * @param index  索引
     * @param type   类型
     * @param idList 数据ID集
     * @return T
     */
    public MultiGetItemResponse[] getByIdList(String index, String type, List<String> idList) {
        MultiGetRequest request = new MultiGetRequest();
        MultiGetRequest.Item item = null;
        for (String id : idList) {
            item = new MultiGetRequest.Item(index, type, id);
            request.add(item);
        }

        try {
            //同步执行
            MultiGetResponse responses = client.mget(request, RequestOptions.DEFAULT);
            return responses.getResponses();
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 根据id查询异常,index = {},type = {},idList = {},stack={}",
                    index, type, idList, e);
            throw new ElasticsearchRunException("ElasticSearch 根据ID批量查询异常");
        }
    }

    /**
     * 根据ID删除数据
     *
     * @param index 索引
     * @param type  类型
     * @param id    数据ID
     * @return 是否删除成功
     */
    public boolean deleteById(String index, String type, String id) {
        DeleteResponse deleteResponse = deleteDataById(index, type, id);
        return RestStatus.OK.equals(deleteResponse.status());
    }

    /**
     * 根据ID删除数据
     *
     * @param index 索引
     * @param type  类型
     * @param id    数据ID
     * @return DeleteResponse
     */
    public DeleteResponse deleteDataById(String index, String type, String id) {
        try {
            DeleteRequest request = new DeleteRequest(index, type, id);
            return client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 根据ID删除异常,index = {},type = {},id = {},stack={}",
                    index, type, id, e);
            throw new ElasticsearchRunException("删除 ElasticSearch 数据异常");
        }
    }

    /**
     * 批量操作-根据ID是否存在,执行新增或更新(同步)
     *
     * @param index    索引
     * @param type     类型
     * @param dataList 数据集
     * @return 返回执行成功的ID集
     */
    public <T> List<String> batchSaveOrUpdate(String index, String type, List<T> dataList) {
        BulkResponse bulkResponse = this.batchSaveOrUpdateData(index, type, dataList);
        if (null == bulkResponse) {
            return new ArrayList<>(0);
        }

        BulkItemResponse[] responseItems = bulkResponse.getItems();
        if (null == responseItems || responseItems.length == 0) {
            return new ArrayList<>(0);
        }

        List<String> successIds = new ArrayList<>();
        for (BulkItemResponse bulkItemResponse : responseItems) {
            if (bulkItemResponse.isFailed()) {
                continue;
            }
            successIds.add(bulkItemResponse.getId());
        }
        return successIds;
    }

    /**
     * 批量操作-根据ID是否存在,执行新增或更新(同步)
     *
     * @param index    索引
     * @param type     类型
     * @param dataList 数据源
     */
    public <T> BulkResponse batchSaveOrUpdateData(String index, String type, List<T> dataList) {
        BulkRequest request = this.buildBulkRequest(index, type, dataList);
        try {
            //批量保存
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            //无失败
            if (!bulkResponse.hasFailures()) {
                return bulkResponse;
            }
            LOGGER.error("[ ElasticSearch-Client ] >> 批量保存部分失败 index = {},type = {},errorMsg = {}",
                    index, type, bulkResponse.buildFailureMessage());
            return bulkResponse;
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 批量保存异常,index = {},type = {},stack={}", index, type, e);
            throw new ElasticsearchRunException("ElasticSearch 批量保存异常");
        }
    }

    /**
     * 批量操作-根据ID是否存在,执行新增或更新(异步)
     *
     * @param index    索引
     * @param type     类型
     * @param dataList 数据集
     */
    public <T> void batchSaveOrUpdateAsync(String index, String type, List<T> dataList) {
        BulkRequest request = this.buildBulkRequest(index, type, dataList);
        //异步执行
        client.bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    LOGGER.error("[ ElasticSearch-Client ] >> 异步批量保存部分失败！index = {},type = {},errorMsg = {}",
                            index, type, bulkResponse.buildFailureMessage());
                }
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("[ ElasticSearch-Client ] >>  异步批量保存执行失败！index = {},type = {}",
                        index, type, e);
            }
        });
    }


    /**
     * 根据多条件查询--分页
     * 注：from-size -[ "浅"分页 ]
     *
     * @param index            索引
     * @param pageFrom         分页开始位置（从0开始）
     * @param pageSize         页容量- Elasticsearch单次最大限制10000行
     * @param boolQueryBuilder 查询条件builder
     * @param sortName         排序字段
     * @param sortOrder        排序规则
     * @return
     * @see SortOrder 排序枚举
     */
    public SearchResponse searchPageByQueryBuilder(String index, Integer pageFrom, Integer pageSize, BoolQueryBuilder boolQueryBuilder, String sortName, SortOrder sortOrder) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(pageFrom);
        searchSourceBuilder.size(pageSize);
        if (!ObjectUtils.isEmpty(sortName)) {
            searchSourceBuilder.sort(sortName, sortOrder);
        }
        searchRequest.source(searchSourceBuilder);
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 根据多条件分页查询异常 index = {},boolQueryBuilder = {}",
                    index, boolQueryBuilder.toString(), e);
            throw new ElasticsearchRunException("ElasticSearch 分页搜索异常");
        }
    }

    /**
     * 根据多条件查询--分页
     * 注：from-size"浅"分页
     *
     * @param index               索引
     * @param searchSourceBuilder 查询条件builder
     * @return
     * @see SortOrder 排序枚举
     */
    public SearchResponse searchPageByQueryBuilder(String index, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 根据多条件分页查询异常 index = {},searchSourceBuilder = {}",
                    index, searchSourceBuilder.toString(), e);
            throw new ElasticsearchRunException("ElasticSearch 分页搜索异常");
        }
    }

    /**
     * 查询所有数据
     * 注：生产环境数据量大-慎用
     *
     * @param index 索引
     * @param type  类型
     */
    public SearchResponse getAllIndex(String index, String type) {
        return getAllIndex(index, type, null, null);
    }


    /**
     * 查询index下所有数据
     * 注：生产环境数据量大-慎用
     *
     * @param index    索引
     * @param type     类型
     * @param includes 显示字段
     * @param excludes 排除字段
     */
    public SearchResponse getAllIndex(String index, String type, String[] includes, String[] excludes) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (null != includes && null != excludes) {
            sourceBuilder.fetchSource(includes, excludes);
        }
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LOGGER.error("[ ElasticSearch-Client ] >> 查询所有数据查询异常 index = {},type = {}",
                    index, type, e);
            throw new ElasticsearchRunException("ElasticSearch 查询index下所有数据异常");
        }
    }


    /**
     * 构建批量保存参数
     */
    private <T> BulkRequest buildBulkRequest(String index, String type, List<T> dataList) {
        BulkRequest request = new BulkRequest();
        JSONObject jsonObject = null;
        IndexRequest indexRequest = null;
        for (Object dataObj : dataList) {
            jsonObject = (JSONObject) JSON.toJSON(dataObj);
            if (jsonObject.containsKey("id")) {
                indexRequest = new IndexRequest(index, type, String.valueOf(jsonObject.get("id")));
            } else {
                indexRequest = new IndexRequest(index, type);
            }
            indexRequest.source(jsonObject.toJSONString(), XContentType.JSON);
            request.add(indexRequest);
        }
        return request;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
