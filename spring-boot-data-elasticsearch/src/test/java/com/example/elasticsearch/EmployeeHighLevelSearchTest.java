package com.example.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.elasticsearch.bean.EmployeeInfo;
import com.example.elasticsearch.service.EmployeeInfoService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ElasticSearch 高级查询示例
 * 本测试类讲解使用类封装ES查询语句示例
 *
 * @author 程序员小强
 */
@SpringBootTest(classes = Application.class)
public class EmployeeHighLevelSearchTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 精确查询
     * 根据姓名name 查询
     */
    @Test
    public void findByNameTest() {
	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
	nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", "张三"));
	SearchHits<EmployeeInfo> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EmployeeInfo.class);
	System.out.println(JSON.toJSONString(search.getSearchHits(), SerializerFeature.PrettyFormat));
    }

    /**
     * AND 语句查询
     * <p>
     * 注：查询姓名为张三，年龄为19的记录
     */
    @Test
    public void findByNameAndAgeTest() {

	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

	//场景 bool 查询对象
	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	boolQueryBuilder.must(QueryBuilders.matchQuery("name", "张三"))
		.must(QueryBuilders.matchQuery("age", 19));

	nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

	SearchHits<EmployeeInfo> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EmployeeInfo.class);
	System.out.println(JSON.toJSONString(search.getSearchHits(), SerializerFeature.PrettyFormat));

    }

    /**
     * OR 语句查询
     * <p>
     * 注：查询姓名为张三，或者年龄为18的记录
     */
    @Test
    public void findByNameOrAgeTest() {

	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

	//场景 bool 查询对象
	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	boolQueryBuilder.should(QueryBuilders.matchQuery("name", "张三"))
		.should(QueryBuilders.matchQuery("age", 18));

	nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

	SearchHits<EmployeeInfo> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EmployeeInfo.class);
	System.out.println(JSON.toJSONString(search.getSearchHits(), SerializerFeature.PrettyFormat));

    }

    /**
     * 聚合查询-groupBy
     */
    @Test
    public void groupByAge() {
	//1.构建查询对象
	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
	nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("groupByAge")
		.field("age").size(30));
	SearchHits<EmployeeInfo> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EmployeeInfo.class);

	Aggregations aggregations = search.getAggregations();

	//解析聚合分组后结果数据
	ParsedLongTerms parsedLongTerms = aggregations.get("groupByAge");
	//groupBy后的年龄集
	List<String> ageList = parsedLongTerms.getBuckets().stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
	System.out.println(ageList);
    }

    /**
     * 分页查询
     * 带参数
     */
    @Test
    public void listPageMatch() {
	int pageNo = 1;
	int pageSize = 5;

	NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
	nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", "小"));

	//注：Pageable类中 pageNum需要减1,如果是第一页 数值为0
	Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
	nativeSearchQueryBuilder.withPageable(pageable);

	SearchHits<EmployeeInfo> searchHitsResult = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EmployeeInfo.class);
	//7.获取分页数据
	SearchPage<EmployeeInfo> searchPageResult = SearchHitSupport.searchPageFor(searchHitsResult, pageable);


	System.out.println("分页查询");
	System.out.println(String.format("totalPages:%d, pageNo:%d, size:%d", searchPageResult.getTotalPages(), pageNo, pageSize));
	System.out.println(JSON.toJSONString(searchPageResult.getSearchHits(), SerializerFeature.PrettyFormat));
    }


}
