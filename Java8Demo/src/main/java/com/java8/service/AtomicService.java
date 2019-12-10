package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-24 13:51
 */
public class AtomicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicService.class);

    private static void atomicIntegerTest() {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        CountDownLatch latch = new CountDownLatch(200);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                latch.countDown();
                LOGGER.info("线程名称:{} 执行完毕，结果为:{}", Thread.currentThread().getName(), atomicInteger.getAndIncrement());
            }).start();
        }
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                latch.countDown();
                LOGGER.info("线程名称:{} 执行完毕，结果为:{}", Thread.currentThread().getName(), atomicInteger.getAndDecrement());
            }).start();
        }

        try {
            latch.await();
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            LOGGER.error("线程被打断", e);
            Thread.currentThread().interrupt();
        }
        LOGGER.info("获取到的值为:{}", atomicInteger.get());
    }

    public static void main(String[] args) {
        atomicIntegerTest();
    }

}
