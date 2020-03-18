package com.example.redis.service.impl;

import com.example.redis.service.PublishTemplateService;
import com.example.util.JsonSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis 发布订阅 -相关操作
 *
 * @author mengqiang
 */
@Component
public class PublishTemplateServiceImpl extends BaseTemplateImpl implements PublishTemplateService {

    @Override
    public Long publish(String key, Object value) {
        validateParam(key, value);
        return redisClient.invoke(jedisPool, jedis -> {
            return jedis.publish(key.getBytes(), JsonSerializer.serialize(value));
        });
    }

}