package com.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:
 * Description: 线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/27 10:22
 */
@Service
public class ExecutorServiceDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceDemo.class);

    public static void test() {
        // 指定核心线程数、最大线程数的大小，线程队列 Integer.MAX_VALUE
        MyThreadFactory fixedThreadFactory = new MyThreadFactory("Fixed");
        ExecutorService executor1 = Executors.newFixedThreadPool(2, fixedThreadFactory);
        fixedThreadFactory.getThreadFactoryStatus();

        // 核心线程数为0，最大线程数为 Integer.MAX_VALUE
        MyThreadFactory cachedThreadFactory = new MyThreadFactory("Cached");
        ExecutorService executor2 = Executors.newCachedThreadPool(cachedThreadFactory);
        cachedThreadFactory.getThreadFactoryStatus();

        // 核心线程数、最大线程数均为1，线程队列长度为 Integer.MAX_VALUE
        MyThreadFactory singleThreadFactory = new MyThreadFactory("Single");
        ExecutorService executor3 = Executors.newSingleThreadExecutor(singleThreadFactory);
        singleThreadFactory.getThreadFactoryStatus();

        // 指定 核心线程数，最大线程数为 Integer.MAX_VALUE
        MyThreadFactory scheduledThreadFactory = new MyThreadFactory("scheduled");
        ExecutorService executor4 = Executors.newScheduledThreadPool(3, scheduledThreadFactory);
        for (int i = 0; i < 20; i++) {
            executor4.submit(() -> {
                try {
                    LOGGER.info("schedule线程初始化为");
                    Thread.currentThread().sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        scheduledThreadFactory.getThreadFactoryStatus();
    }


    public static void main(String[] args) {
        test();
    }

}
