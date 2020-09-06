package com.example.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.example.model.OrderInfoModel;
import com.example.model.UserInfoModel;
import com.example.util.DateUtil;
import com.example.util.MqListenerHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 订单-消息监听
 */
@Slf4j
@Component
public class OrderNormalMsgListener implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("********** 订单消息消费 start **********");
        log.info("消息 id={},执行Host={}", message.getMsgID(), message.getBornHost());
        log.info("消息 Topic={},Tag={}", message.getTopic(), message.getTag());
        log.info("消息生成时间={}", DateUtil.formatDateMillisecond(new Date(message.getBornTimestamp())));
        log.info("消息执行次数={}", message.getReconsumeTimes());

        try {
            OrderInfoModel orderInfoModel = MqListenerHelperUtil.parseObjectMsg(message, OrderInfoModel.class);
            log.info("消息内容={}", orderInfoModel);
            log.info("********** 订单消息消费 end **********");
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }

}
