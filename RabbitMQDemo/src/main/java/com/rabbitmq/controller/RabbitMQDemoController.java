package com.rabbitmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.config.RabbitMQConfig;
import com.rabbitmq.controller.api.RabbitMQDemoApi;
import com.rabbitmq.service.RabbitMQProducerService;
import com.rabbitmq.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/16 16:06
 */
@RequestMapping(value = "rabbitMQSend")
@RestController
public class RabbitMQDemoController implements RabbitMQDemoApi {

    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;

    @Override
    @PostMapping(value = "sendTopicMsg")
    public JSONObject sendTopicMsg(String msg, String uniqueId, String exchangeName, String routingKey) {

        if (StringUtils.isEmpty(uniqueId)) {
            uniqueId = DateUtils.getFormatDateStr(new Date(), DateUtils.DATE_FORMAT_STR_3);
        }
        if (StringUtils.isEmpty(exchangeName)) {
            exchangeName = RabbitMQConfig.EXCHANGE_TOPIC_1;
        }
        if (StringUtils.isEmpty(routingKey)) {
            routingKey = RabbitMQConfig.ROUTING_KEY_3;
        }

        String response = rabbitMQProducerService.sendMsg(msg, uniqueId, exchangeName, routingKey);
        JSONObject result = new JSONObject();
        result.put("isSuccess", true);
        result.put("data", response);
        result.put("resultMsg", "Success");
        return result;
    }
}
