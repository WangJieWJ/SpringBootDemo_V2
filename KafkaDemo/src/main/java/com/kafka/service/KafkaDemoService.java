package com.kafka.service;

import com.alibaba.fastjson.JSON;
import com.kafka.config.KafkaConfig;
import com.kafka.pojo.MessageData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.UUID;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/10 09:36
 */
@Service
public class KafkaDemoService {

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDemoService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public ListenableFuture<SendResult> producerMsg(String topicName, Integer partition, String key, String msg) {
        MessageData messageData = new MessageData();
        messageData.setId(UUID.randomUUID().toString());
        messageData.setMsg(msg);
        messageData.setSendTime(new Date());
        return kafkaTemplate.send(topicName, partition, key, JSON.toJSONString(messageData));
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_1_NAME}, clientIdPrefix = "consumerDefaultGroupMsg")
    public void consumerDefaultGroupMsg(ConsumerRecord<String, String> record) {
        LOGGER.info("\ngroup:defaultGroup消息监听：topic:{},key:{},value:{},offset:{},partition:{}," +
                        "serializedKeySize:{},serializedValueSize:{},timestamp:{},timestampType:{}",
                record.topic(), record.key(), record.value(), record.offset(), record.partition(),
                record.serializedKeySize(), record.serializedValueSize(),
                record.timestamp(), record.timestampType());
    }


    @KafkaListener(topics = {KafkaConfig.TOPIC_1_NAME}, groupId = KafkaConfig.GROUP_ONE, clientIdPrefix = "consumerGroupOneMsg")
    public void consumerGroupOneMsg(ConsumerRecord<String, String> record) {
        LOGGER.info("\ngroup:{}消息监听：topic:{},key:{},value:{},offset:{},partition:{}," +
                        "serializedKeySize:{},serializedValueSize:{},timestamp:{},timestampType:{}",
                KafkaConfig.GROUP_ONE, record.topic(), record.key(), record.value(), record.offset(), record.partition(),
                record.serializedKeySize(), record.serializedValueSize(),
                record.timestamp(), record.timestampType());
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_1_NAME}, groupId = KafkaConfig.GROUP_SECOND, clientIdPrefix = "consumerGroupSecondMsg_1")
    public void consumerGroupSecondMsg_1(ConsumerRecord<String, String> record) {
        LOGGER.info("\n测试同一条消息只会被分组内的一个消费者消费group:{}消息监听：topic:{},key:{},value:{},offset:{},partition:{}," +
                        "serializedKeySize:{},serializedValueSize:{},timestamp:{},timestampType:{}",
                KafkaConfig.GROUP_SECOND, record.topic(), record.key(), record.value(), record.offset(), record.partition(),
                record.serializedKeySize(), record.serializedValueSize(),
                record.timestamp(), record.timestampType());
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_1_NAME}, groupId = KafkaConfig.GROUP_SECOND, clientIdPrefix = "consumerGroupSecondMsg_2")
    public void consumerGroupSecondMsg_2(ConsumerRecord<String, String> record) {
        LOGGER.info("\n测试同一条消息只会被分组内的一个消费者消费group:{}消息监听：topic:{},key:{},value:{},offset:{},partition:{}," +
                        "serializedKeySize:{},serializedValueSize:{},timestamp:{},timestampType:{}",
                KafkaConfig.GROUP_SECOND, record.topic(), record.key(), record.value(), record.offset(), record.partition(),
                record.serializedKeySize(), record.serializedValueSize(),
                record.timestamp(), record.timestampType());
    }

}
