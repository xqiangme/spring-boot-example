package com.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerListener {

    @KafkaListener(topics = "test_topics")
    public void onMessage(String message) {

        log.info("接收消息 KafKa message:{}", message);
    }

}