package com.example.config.mq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.example.config.property.MqBaseProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * RocketMQ 消息队列-配置
 *
 * @author 程序员小强
 */
@Slf4j
public class MqBaseClient {

    @Autowired
    private MqBaseProperty mqBaseProperty;

    /**
     * 创建生产者
     *
     * @return 生产者bean
     * @author 程序员小强
     */
    protected ProducerBean createProducer() {
        ProducerBean producer = new ProducerBean();
        Properties properties = mqBaseProperty.getMqProperties();
        //重试次数
        properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, "16");
        //超时时间
        properties.setProperty(PropertyKeyConst.ConsumeTimeout, "3");
        producer.setProperties(properties);
        log.info("[ RocketMQ ] >> Producer create success");
        return producer;
    }

    /**
     * 创建集群订阅消费者
     *
     * @param groupId          分组id
     * @param consumeThreadNum 最大消费线程数
     * @return 消费者bean
     * @author 程序员小强
     */
    protected ConsumerBean createConsumer(String groupId, Integer consumeThreadNum,
                                          Map<Subscription, MessageListener> subscriptionTable) {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqBaseProperty.getMqProperties();
        //分组ID
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //消费者线程数
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(consumeThreadNum));
        consumerBean.setProperties(properties);
        //订阅关系 > 订阅多个topic如上面设置
        consumerBean.setSubscriptionTable(subscriptionTable);
        log.info("[ RocketMQ ] >> 集群订阅消费 create success , groupId:{},consumeThreadNum:{}", groupId, consumeThreadNum);
        return consumerBean;
    }

    /**
     * 创建广播模式消费者
     *
     * @param groupId           分组id
     * @param consumeThreadNum  固定消费者线程数为xx个
     * @param subscriptionTable 消费监听Map
     * @return 消费者bean
     * @author 程序员小强
     */
    protected ConsumerBean createBroadCastConsumer(String groupId, Integer consumeThreadNum,
                                                   Map<Subscription, MessageListener> subscriptionTable) {

        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqBaseProperty.getMqProperties();
        //分组ID
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(consumeThreadNum));
        // 广播订阅方式设置
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        consumerBean.setProperties(properties);
        //订阅关系 > 订阅多个topic如上面设置
        consumerBean.setSubscriptionTable(subscriptionTable);
        log.info("[ RocketMQ ] >> 广播订阅消费 create success , groupId:{},consumeThreadNum:{}", groupId, consumeThreadNum);
        return consumerBean;
    }

    protected Map<Subscription, MessageListener> createSubscriptionTable(String topic, String tag, MessageListener msgListener) {
        //订阅关系-支持多个
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>(2);
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        subscriptionTable.put(subscription, msgListener);
        return subscriptionTable;
    }
}