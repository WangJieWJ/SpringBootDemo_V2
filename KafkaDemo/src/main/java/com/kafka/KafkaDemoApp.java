package com.kafka;

import com.kafka.publisher.UserEventPublisher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Random;
import java.util.UUID;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/9 17:51
 */
@SpringBootApplication
public class KafkaDemoApp implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        new SpringApplicationBuilder(KafkaDemoApp.class).web(true).run(args);
        applicationContext.getBean(UserEventPublisher.class).publish((new Random()).nextLong(), UUID.randomUUID().toString(), (new Random()).nextLong(), UUID.randomUUID().toString());
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
