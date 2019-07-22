package com.rabbitmq.service;

import com.rabbitmq.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Service;

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
public class RabbitMQConsumerService {


    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    @RabbitListeners({@RabbitListener(queues = RabbitMQConfig.QUEUE_1),
            @RabbitListener(queues = RabbitMQConfig.QUEUE_2)})
    public String processMessage1(String msg) {

        LOGGER.info("监听队列1、队列2,接收到的消息为:{}", msg);

        return "队列1、队列2消息接收成功！";
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_1)
    public String processMessage2(String msg) {

        LOGGER.info("监听队列1,接收到的消息为:{}", msg);
        return "队列1消息接收成功！";
    }

}
