package com.example.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.exception.ElasticSearchRunException;
import com.example.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch 客户端 RestHighLevelClient Api接口封装
 * <p>
 * 官方Api地址：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.x/java-rest-high.html
 *
 * @author 程序员小强
 * @date 2020-12-06 22:46
 */
@Slf4j
@Component
public class ElasticSearchRestApiClient {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 默认主分片数
     */
    private static final int DEFAULT_SHARDS = 3;
    /**
     * 默认副本分片数
     */
    private static final int DEFAULT_REPLICAS = 2;

    /**
     * 判断索引是否存在
     *
     * @param index 索引
     * @return 返回 true，表示存在
     */
    public boolean existsIndex(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            request.local(false);
            request.humanReadable(true);
            request.includeDefaults(false);

            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> get index exists exception ,index:{} ", index, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> get index exists exception {}", e);
        }
    }

    /**
     * 创建 ES 索引
     *
     * @param index      索引
     * @param properties 文档属性集合
     * @return 返回 true，表示创建成功
     */
    public boolean createIndex(String index, Map<String, Object> properties) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            // 注：ES 7.x 后的版本中，已经弃用 type
            builder.startObject()
                    .startObject("mappings")
                    .field("properties", properties)
                    .endObject()
                    .startObject("settings")
                    //分片数
                    .field("number_of_shards", DEFAULT_SHARDS)
                    //副本数
                    .field("number_of_replicas", DEFAULT_REPLICAS)
                    .endObject()
                    .endObject();
            CreateIndexRequest request = new CreateIndexRequest(index).source(builder);
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> createIndex exception ,index:{},properties:{}", index, properties, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> createIndex exception ");
        }
    }

    /**
     * 删除索引
     *
     * @param index 索引
     * @return 返回 true，表示删除成功
     */
    public boolean deleteIndex(String index) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);

            return response.isAcknowledged();
        } catch (ElasticsearchException e) {
            //索引不存在-无需删除
            if (e.status() == RestStatus.NOT_FOUND) {
                log.error("[ elasticsearch ] >>  deleteIndex >>  index:{}, Not found ", index, e);
                return false;
            }
            log.error("[ elasticsearch ] >> deleteIndex exception ,index:{}", index, e);
            throw new ElasticSearchRunException("elasticsearch deleteIndex exception ");
        } catch (IOException e) {
            //其它未知异常
            log.error("[ elasticsearch ] >> deleteIndex exception ,index:{}", index, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >>  deleteIndex exception {}", e);
        }
    }

    /**
     * 获取索引配置
     *
     * @param index 索引
     * @return 返回索引配置内容
     */
    public GetSettingsResponse getIndexSetting(String index) {
        try {
            GetSettingsRequest request = new GetSettingsRequest().indices(index);
            return restHighLevelClient.indices().getSettings(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            //其它未知异常
            log.error("[ elasticsearch ] >> getIndexSetting exception ,index:{}", index, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >>  getIndexSetting exception {}", e);
        }
    }


    /**
     * 判断文档是否存在
     *
     * @param index 索引
     * @return 返回 true，表示存在
     */
    public boolean existsDocument(String index, String id) {
        try {
            GetRequest request = new GetRequest(index, id);
            //禁用获取_source
            request.fetchSourceContext(new FetchSourceContext(false));
            //禁用获取存储的字段。
            request.storedFields("_none_");

            return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> get document exists exception ,index:{} ", index, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> get document  exists exception {}", e);
        }
    }

    /**
     * 保存数据-随机生成数据ID
     *
     * @param index     索引
     * @param dataValue 数据内容
     */
    public IndexResponse save(String index, Object dataValue) {
        try {
            IndexRequest request = new IndexRequest(index);
            request.source(JSON.toJSONString(dataValue), XContentType.JSON);
            return restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> save exception ,index = {},dataValue={} ,stack={}", index, dataValue, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> save exception {}", e);
        }
    }

    /**
     * 保存文档-自定义数据ID
     *
     * @param index     索引
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public IndexResponse save(String index, String id, Object dataValue) {
        return this.saveOrUpdate(index, id, dataValue);
    }

    /**
     * 保存文档-自定义数据ID
     * <p>
     * 如果文档存在，则更新文档；如果文档不存在，则保存文档。
     *
     * @param index     索引
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public IndexResponse saveOrUpdate(String index, String id, Object dataValue) {
        try {
            IndexRequest request = new IndexRequest(index);
            request.id(id);
            request.source(JSON.toJSONString(dataValue), XContentType.JSON);
            return restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> save exception ,index = {},dataValue={} ,stack={}", index, dataValue, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> save exception {}", e);
        }
    }

    /**
     * 批量-新增或保存文档
     * <p>
     * 如果集合中有些文档已经存在，则更新文档；不存在，则保存文档。
     *
     * @param index        索引
     * @param documentList 文档集合
     */
    public void batchSaveOrUpdate(String index, List<ElasticSearchDocModel<?>> documentList) {
        if (CollectionUtils.isEmpty(documentList)) {
            return;
        }
        try {
            // 批量请求
            BulkRequest bulkRequest = new BulkRequest();
            documentList.forEach(doc -> bulkRequest.add(new IndexRequest(index)
                    .id(doc.getId())
                    .source(JSON.toJSONString(doc.getData()), XContentType.JSON)));
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> batchSave exception ,index = {},documentList={} ,stack={}", index, documentList, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> batchSave exception {}", e);
        }
    }

    /**
     * 根据ID修改
     *
     * @param index     索引
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public UpdateResponse updateById(String index, String id, Object dataValue) {
        try {
            UpdateRequest request = new UpdateRequest(index, id);
            request.doc(JSON.toJSONString(dataValue), XContentType.JSON);
            return restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> updateById exception ,index = {},dataValue={} ,stack={}", index, dataValue, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> updateById exception {}", e);
        }
    }

    /**
     * 部分修改()
     * 注：1).可变更已有字段值，可新增字段，删除字段无效
     * 2).若当前ID数据不存在则新增
     *
     * @param index     索引
     * @param id        数据ID
     * @param dataValue 数据内容
     */
    public UpdateResponse updateByIdSelective(String index, String id, Object dataValue) {
        try {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(dataValue));
            UpdateRequest request = new UpdateRequest(index, id)
                    .doc(jsonObject)
                    .upsert(jsonObject);
            return restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> updateByIdSelective exception ,index = {},dataValue={} ,stack={}", index, dataValue, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> updateByIdSelective exception {}", e);
        }
    }

    /**
     * 根据id查询
     *
     * @param index 索引
     * @param id    数据ID
     * @return T
     */
    public <T> T getById(String index, String id, Class<T> clazz) {
        GetResponse getResponse = this.getById(index, id);
        if (null == getResponse) {
            return null;
        }
        return JSON.parseObject(getResponse.getSourceAsString(), clazz);
    }

    /**
     * 根据id集批量获取数据
     *
     * @param index  索引
     * @param idList 数据ID集
     * @return T
     */
    public <T> List<T> getByIdList(String index, List<String> idList, Class<T> clazz) {
        MultiGetItemResponse[] responses = this.getByIdList(index, idList);
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
     * 根据多条件查询--分页
     * 注：from-size -[ "浅"分页 ]
     *
     * @param index    索引
     * @param pageNo   页码（第几页）
     * @param pageSize 页容量- Elasticsearch默认配置单次最大限制10000
     */
    public <T> List<T> searchPageByIndex(String index, Integer pageNo, Integer pageSize, Class<T> clazz) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(PageUtils.getStartRow(pageNo, pageSize));
        searchSourceBuilder.size(pageSize);

        return this.searchByQuery(index, searchSourceBuilder, clazz);
    }

    /**
     * 条件查询
     *
     * @param index         索引
     * @param sourceBuilder 条件查询构建起
     * @param <T>           数据类型
     * @return T 类型的集合
     */
    public <T> List<T> searchByQuery(String index, SearchSourceBuilder sourceBuilder, Class<T> clazz) {
        try {
            // 构建查询请求
            SearchRequest searchRequest = new SearchRequest(index).source(sourceBuilder);
            // 获取返回值
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            if (null == hits || hits.length == 0) {
                return new ArrayList<>(0);
            }

            List<T> resultList = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                resultList.add(JSON.parseObject(hit.getSourceAsString(), clazz));
            }
            return resultList;
        } catch (ElasticsearchStatusException e) {
            //索引不存在
            if (e.status() == RestStatus.NOT_FOUND) {
                log.error("[ elasticsearch ] >>  searchByQuery exception >>  index:{}, Not found ", index, e);
                return new ArrayList<>(0);
            }
            throw new ElasticSearchRunException("[ elasticsearch ] >> searchByQuery exception {}", e);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> searchByQuery exception ,index = {},sourceBuilder={} ,stack={}", index, sourceBuilder, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> searchByQuery exception {}", e);
        }
    }


    /**
     * 根据ID删除文档
     *
     * @param index 索引
     * @param id    文档ID
     * @return 是否删除成功
     */
    public boolean deleteById(String index, String id) {
        try {
            DeleteRequest request = new DeleteRequest(index, id);
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            //未找到文件
            if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.error("[ elasticsearch ] >> deleteById document is not found , index:{},id:{}", index, id);
                return false;
            }
            return RestStatus.OK.equals(response.status());
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> deleteById exception ,index:{},id:{} ,stack:{}", index, id, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> deleteById exception {}", e);
        }
    }

    /**
     * 根据查询条件删除文档
     *
     * @param index        索引
     * @param queryBuilder 查询条件构建器
     */
    public void deleteByQuery(String index, QueryBuilder queryBuilder) {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(index).setQuery(queryBuilder);
            request.setConflicts("proceed");
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> deleteByQuery exception ,index = {},queryBuilder={} ,stack={}", index, queryBuilder, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> deleteByQuery exception {}", e);
        }
    }

    /**
     * 根据文档 ID 批量删除文档
     *
     * @param index  索引
     * @param idList 文档 ID 集合
     */
    public void deleteByIdList(String index, List<String> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        try {
            BulkRequest bulkRequest = new BulkRequest();
            idList.forEach(id -> bulkRequest.add(new DeleteRequest(index, id)));
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> deleteByIdList exception ,index = {},idList={} ,stack={}", index, idList, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> deleteByIdList exception {}", e);
        }
    }

    /**
     * 根据id查询
     *
     * @param index 索引
     * @param id    文档ID
     * @return GetResponse
     */
    private GetResponse getById(String index, String id) {
        try {
            GetRequest request = new GetRequest(index, id);
            return restHighLevelClient.get(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                log.error("[ elasticsearch ] >> getById document not found ,index = {},id={} ,stack={}", index, id, e);
                return null;
            }
            throw new ElasticSearchRunException("[ elasticsearch ] >> getById exception {}", e);
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> getById exception ,index = {},id={} ,stack={}", index, id, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> getById exception {}", e);
        }
    }

    /**
     * 根据id集-批量获取数据
     *
     * @param index  索引
     * @param idList 数据文档ID集
     * @return MultiGetItemResponse[]
     */
    private MultiGetItemResponse[] getByIdList(String index, List<String> idList) {
        try {
            MultiGetRequest request = new MultiGetRequest();
            for (String id : idList) {
                request.add(new MultiGetRequest.Item(index, id));
            }

            //同步执行
            MultiGetResponse responses = restHighLevelClient.mget(request, RequestOptions.DEFAULT);
            return responses.getResponses();
        } catch (IOException e) {
            log.error("[ elasticsearch ] >> getByIdList exception ,index = {},idList={} ,stack={}", index, idList, e);
            throw new ElasticSearchRunException("[ elasticsearch ] >> getByIdList exception {}", e);
        }
    }
}
