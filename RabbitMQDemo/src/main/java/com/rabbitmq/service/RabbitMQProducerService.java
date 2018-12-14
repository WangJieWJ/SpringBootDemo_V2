package com.rabbitmq.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/13 20:40
 */
@Service
public class RabbitMQProducerService implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitTemplate.class);

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            LOGGER.info("消息发送成功,correlationData:{},cause:{}", JSON.toJSONString(correlationData), cause);
        } else {
            LOGGER.error("消息发送失败,correlationData:{},cause:{}", JSON.toJSONString(correlationData), cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        LOGGER.info("消息发送失败,message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",
                JSON.toJSONString(message), replyCode, replyText, exchange, routingKey);
    }

    public void sendMsg(String msg, String uniqueId, String exchangeName, String routingKey) {
        LOGGER.info("开始向exchange:{},发送消息msg:{},并携带routingKey:{},唯一标示uniqueId:{}",
                exchangeName, msg, routingKey, uniqueId);
        CorrelationData correlationData = new CorrelationData(uniqueId);
        String response = rabbitTemplate.convertSendAndReceive(exchangeName, routingKey, msg, correlationData).toString();
        LOGGER.info("消费者响应response:{},唯一标示uniqueId:{}", response, uniqueId);
    }
}
