package com.example.redis;

import com.example.redis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mengqiang
 */
@Component
public class RedisCache {

    /**
     * 分布式锁
     */
    private RedisLockService redisLockService;

    /**
     * 键命令-key -相关操作
     */
    private KeyTemplateService keyTemplateService;

    /**
     * String(字符串) 数据类型 -相关操作
     */
    private StringTemplateService stringTemplateService;

    /**
     * Set 数据类型 -相关操作
     */
    private SetTemplateService setTemplateService;

    /**
     * List(集合) -数据类型-相关操作
     */
    private ListTemplateService listTemplateService;

    /**
     * Hash -数据类型-相关操作
     */
    private HashTemplateService hashTemplateService;

    /**
     * Zset(有些集合) 数据类型 - 相关操作
     */
    private ZsetTemplateService zsetTemplateService;

    /**
     * Pipeline 管道技术 -相关操作
     */
    private PipelineTemplateService pipelineTemplateService;

    /**
     * 发布订阅 -相关操作
     */
    private PublishTemplateService publishTemplateService;

    @Autowired
    public RedisCache(RedisLockService redisLockService, KeyTemplateService keyTemplateService,
                      StringTemplateService stringTemplateService, ListTemplateService listTemplateService,
                      SetTemplateService setTemplateService, HashTemplateService hashTemplateService,
                      ZsetTemplateService zsetTemplateService, PipelineTemplateService pipelineTemplateService,
                      PublishTemplateService publishTemplateService) {
        this.redisLockService = redisLockService;
        this.keyTemplateService = keyTemplateService;
        this.stringTemplateService = stringTemplateService;
        this.listTemplateService = listTemplateService;
        this.setTemplateService = setTemplateService;
        this.hashTemplateService = hashTemplateService;
        this.zsetTemplateService = zsetTemplateService;
        this.pipelineTemplateService = pipelineTemplateService;
        this.publishTemplateService = publishTemplateService;
    }

    public RedisLockService lock() {
        return this.redisLockService;
    }

    public KeyTemplateService keyTemplate() {
        return this.keyTemplateService;
    }

    public StringTemplateService stringTemplate() {
        return this.stringTemplateService;
    }

    public ListTemplateService listTemplate() {
        return this.listTemplateService;
    }

    public SetTemplateService setTemplate() {
        return this.setTemplateService;
    }

    public HashTemplateService hashTemplate() {
        return this.hashTemplateService;
    }

    public ZsetTemplateService zSetTemplate() {
        return this.zsetTemplateService;
    }

    public PipelineTemplateService pipelineTemplate() {
        return this.pipelineTemplateService;
    }

    public PublishTemplateService publishTemplate() {
        return this.publishTemplateService;
    }

}
