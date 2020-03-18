package com.example.redis.service.impl;

import com.example.redis.service.PipelineTemplateService;
import com.example.util.JsonSerializer;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 管道查询 - Pipeline -相关操作
 *
 * @author mengqiang
 */
@Component
public class PipelineTemplateServiceImpl extends BaseTemplateImpl implements PipelineTemplateService {

    @Override
    public <T> List<T> zRevRangePipeline(String key, Class<T> tClass, List<Integer> starts) {
        List<T> result = redisClient.invoke(jedisPool, jedis -> {
            Pipeline pipeline = jedis.pipelined();

            List<Response<Set<byte[]>>> responseList = new ArrayList<>();
            for (Integer start : starts) {
                Response<Set<byte[]>> response = pipeline.zrevrange(key.getBytes(), start, start);
                responseList.add(response);
            }
            pipeline.sync();
            if (CollectionUtils.isEmpty(responseList)) {
                return new ArrayList<T>();
            }
            List<T> list = new ArrayList<>(responseList.size());
            for (Response<Set<byte[]>> response : responseList) {
                for (byte[] bytes : response.get()) {
                    T t = JsonSerializer.deserialize(bytes, tClass);
                    list.add(t);
                }
            }
            return list;
        });
        return result;
    }

    @Override
    public List<Map<String, String>> hgetallPipeline(List<String> hashKeys) {
        List<Map<String, String>> result = redisClient.invoke(jedisPool, jedis -> {
            Pipeline pipeline = jedis.pipelined();

            List<Response<Map<String, String>>> responseList = Lists.newArrayList();
            for (String hashKey : hashKeys) {
                responseList.add(pipeline.hgetAll(hashKey));
            }
            pipeline.sync();
            if (CollectionUtils.isEmpty(responseList)) {
                return new ArrayList<>();
            }
            List<Map<String, String>> list = Lists.newArrayList();
            for (Response<Map<String, String>> response : responseList) {
                list.add(response.get());
            }
            return list;
        });
        return result;
    }

    @Override
    public List<String> hmgetallPipeline(List<String> hashKeys, String fieldKey) {
        List<String> result = redisClient.invoke(jedisPool, jedis -> {
            Pipeline pipeline = jedis.pipelined();

            List<Response<List<String>>> responseList = Lists.newArrayList();
            for (String hashKey : hashKeys) {
                responseList.add(pipeline.hmget(hashKey, fieldKey));
            }
            pipeline.sync();
            if (CollectionUtils.isEmpty(responseList)) {
                return new ArrayList<>();
            }
            List<String> list = Lists.newArrayList();
            for (Response<List<String>> response : responseList) {
                if (null != response.get().get(0)) {
                    list.add(response.get().get(0));
                }
            }
            return list;
        });
        return result;
    }


}