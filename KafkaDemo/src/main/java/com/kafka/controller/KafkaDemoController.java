package com.kafka.controller;

import com.alibaba.fastjson.JSONObject;
import com.kafka.controller.api.KafkaDemoApi;
import com.kafka.service.KafkaDemoService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/9 18:05
 */
@RestController
@RequestMapping(value = "/kafka")
public class KafkaDemoController implements KafkaDemoApi {

    @Autowired
    private KafkaDemoService kafkaDemoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDemoController.class);

    @Override
    @PostMapping(value = "/producerMsg")
    public JSONObject producerMsg(String topicName, Integer partition, String key, String msg) {
        JSONObject resultData = new JSONObject();
        try {
            ListenableFuture<SendResult> listenableFuture = kafkaDemoService.producerMsg(topicName, partition, key, msg);
            listenableFuture.addCallback(success -> {
                LOGGER.info("消息发送成功！");
            }, fail -> {
                LOGGER.error("消息发送失败！");
            });
            SendResult sendResult = listenableFuture.get();
            ProducerRecord producerRecord = sendResult.getProducerRecord();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
            resultData.put("producerRecord_topic", producerRecord.topic());
            resultData.put("producerRecord_value", producerRecord.value());
            resultData.put("producerRecord_key", producerRecord.key());
            resultData.put("producerRecord_partition", producerRecord.partition());
            resultData.put("producerRecord_timestamp", producerRecord.timestamp());
            resultData.put("recordMetadata_serializedValueSize", recordMetadata.serializedValueSize());
            resultData.put("recordMetadata_checksum", recordMetadata.checksum());
            resultData.put("recordMetadata_offset()", recordMetadata.offset());
            resultData.put("recordMetadata_partition()", recordMetadata.partition());
            resultData.put("recordMetadata_topic", recordMetadata.topic());
            resultData.put("returnMsg", "消息发送成功！");
        } catch (Exception e) {
            resultData.put("returnMsg", "消息发送失败！");
            LOGGER.error("消息发送失败！", e);
        }
        return resultData;
    }
}
