package com.example.elasticsearch.repository;

import com.example.elasticsearch.bean.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 程序员小强
 * @date 2020-12-08 16:34
 */
public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Integer> {

}
