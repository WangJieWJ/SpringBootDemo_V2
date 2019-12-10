package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title:
 * Description: 循环队列
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-20 13:50
 */
public class LockAndConditionService {

    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();
    final Condition notFully = lock.newCondition();

    final String[] items = new String[100];
    int putptr, takeptr, count;

    private static final Logger LOGGER = LoggerFactory.getLogger(LockAndConditionService.class);

    /**
     * 向循环队列中存放数据
     *
     * @param str 数据
     * @throws InterruptedException
     */
    public void put(String str) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                LOGGER.info("已达到队列最大长度，开始等待");
                notFully.await();
            }
            items[putptr] = str;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            LOGGER.info("成功生产数据");
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从循环队列中取值
     *
     * @return 获取到的值
     * @throws InterruptedException
     */
    public String take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                LOGGER.info("队列中没有需要消费的数据，开始等待");
                notEmpty.await();
            }
            String x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            LOGGER.info("成功消费数据");
            notFully.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockAndConditionService lockAndConditionService = new LockAndConditionService();
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10000; i++) {
                    lockAndConditionService.put(String.format("生产数据:%s", i));
                }
            } catch (InterruptedException e) {
                LOGGER.error("生产报错", e);
            }
        },"producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10000; i++) {
                    lockAndConditionService.take();
                    Thread.sleep(1000L);
                }
            } catch (InterruptedException e) {
                LOGGER.error("消费报错", e);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
