package com.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title: rabbitMQ配置类
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/13 17:31
 */
@Configuration
public class RabbitMQConfig {

    //声明队列名称
    public static final String QUEUE_1 = "hello.queue1";
    public static final String QUEUE_2 = "hello.queue2";

    //声明交换器名称
    public static final String EXCHANGE_TOPIC_1 = "topicExchange";

    //routing_Key
    public static final String ROUTING_KEY_1 = "routingKey.1";
    public static final String ROUTING_KEY_2 = "routingKey.#";
    public static final String ROUTING_KEY_3 = "routingKey.3";


    //声明队列
    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_1, true); //true表示持久化该队列
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_2, true);
    }

    //声明交换器
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC_1);
    }

    //绑定
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with(ROUTING_KEY_1);
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with(ROUTING_KEY_2);
    }

}
