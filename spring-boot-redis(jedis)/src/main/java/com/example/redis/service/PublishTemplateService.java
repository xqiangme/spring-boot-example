package com.example.redis.service;

/**
 * Redis 发布订阅 -相关操作
 *
 * @author mengqiang
 */
public interface PublishTemplateService {

    Long publish(String key, Object value);
}