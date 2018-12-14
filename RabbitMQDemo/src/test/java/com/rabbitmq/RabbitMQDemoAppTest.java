package com.rabbitmq;

import com.rabbitmq.config.RabbitMQConfig;
import com.rabbitmq.service.RabbitMQProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/13 17:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQDemoAppTest {

    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;

    @Test
    public void contextLoads() throws InterruptedException {
        String uniqueId = "";
        while (true) {
            uniqueId = String.valueOf(System.currentTimeMillis());
            rabbitMQProducerService.sendMsg("吃饭了吗？" + uniqueId, uniqueId,
                    RabbitMQConfig.EXCHANGE_TOPIC_1, RabbitMQConfig.ROUTING_KEY_3);
            Thread.sleep(1000L);

            uniqueId = String.valueOf(System.currentTimeMillis());
            rabbitMQProducerService.sendMsg("吃饭了吗？" + uniqueId, uniqueId,
                    RabbitMQConfig.EXCHANGE_TOPIC_1, RabbitMQConfig.ROUTING_KEY_3);
            Thread.sleep(1000L);
        }

    }
}