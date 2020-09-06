package com.example.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 消息队列-配置
 * 主题（topic）
 * 分组ID（groupId）
 * 标签（tag）
 *
 * @author 程序员小强
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MqProperty {

    private String topic;
    private String groupId;
    private String tag;
    private Integer consumeThreadNum;

    private String orderTopic;
    private String orderGroupId;
    private String orderTag;
    /**
     * 订单消费者线程数，默认1
     */
    private Integer orderConsumeThreadNum;

}
