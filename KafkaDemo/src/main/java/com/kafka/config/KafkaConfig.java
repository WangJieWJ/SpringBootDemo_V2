package com.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/10 13:37
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    // 定义topic的名称
    public static final String TOPIC_1_NAME = "topic_1";
    // 定义topic的分区大小
    public static final Integer TOPIC_1_NUM_PARTITIONS = 1;
    // 定义topic的备份数量(复制因子)
    public static final short TOPIC_1_REPLICATION_FACTOR = 1;

    public static final String TOPIC_2_NAME = "topic_2";
    public static final Integer TOPIC_2_NUM_PARTITIONS = 4;
    public static final short TOPIC_2_REPLICATION_FACTOR = 1;

    public static final String BATCH_TOPIC_NAME = "batch_topic";
    public static final Integer BATCH_TOPIC_NUM_PARTITIONS = 8;
    public static final short BATCH_TOPIC_REPLICATION_FACTOR = 1;


    public static final String GROUP_ONE = "groupOne";

    public static final String GROUP_SECOND = "groupSecond";

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean("batchContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaBatchListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(batchConsumerProps()));
        //设置并发量，小于或等于Topic的分区数，此处分区数为8
        factory.setConcurrency(5);
        //设置为批量监听
        factory.setBatchListener(true);
        return factory;
    }

    //根据consumerProps填写的参数创建消费者工厂
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    //根据senderProps填写的参数创建生产者工厂
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    //kafkaTemplate实现了Kafka发送接收等功能
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate template = new KafkaTemplate<>(producerFactory());
        return template;
    }

    //消费者配置参数
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        //连接地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9091,127.0.0.1:9092,127.0.0.1:9093");
        //GroupID
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bootKafka");
        //是否自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //自动提交的频率
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        //Session超时设置
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        //键的反序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //值的反序列化方式
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    //批量消费者配置参数
    private Map<String, Object> batchConsumerProps() {
        Map<String, Object> props = consumerProps();
        //一次拉取消息数量 因为并发量为5，所以此处可以一次获取5条数据
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "5");
        return props;
    }

    //生产者配置
    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        //连接地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9091");
        //重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        //控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        //生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
        //键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        //自动生成Topic
        return new KafkaAdmin(senderProps());
    }

    @Bean
    public NewTopic createTopic_1() {
        return new NewTopic(TOPIC_1_NAME, TOPIC_1_NUM_PARTITIONS, TOPIC_1_REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic createTopic_2() {
        return new NewTopic(TOPIC_2_NAME, TOPIC_2_NUM_PARTITIONS, TOPIC_2_REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic createBatchTopic() {
        return new NewTopic(BATCH_TOPIC_NAME, BATCH_TOPIC_NUM_PARTITIONS, BATCH_TOPIC_REPLICATION_FACTOR);
    }
}
