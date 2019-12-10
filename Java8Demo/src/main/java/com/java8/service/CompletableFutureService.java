package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-25 17:22
 */
public class CompletableFutureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureService.class);

    private static void completableFutureTest() throws ExecutionException, InterruptedException {
        // 使用Runnable、默认的线程池
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000L);
                LOGGER.info("使用默认线程池、Runnable的CompletableFuture执行完毕");
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断", e);
                Thread.currentThread().interrupt();
            }
        });
        completableFuture.get();

        // 使用Supplier、默认的线程池
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程被异常打断", e);
                Thread.currentThread().interrupt();
            }
            return "SUCCESS";
        });
        LOGGER.info("使用默认线程池、Supplier的CompletableFuture执行完毕，返回结果为:{}", completableFuture1.get());

        // 使用Runnable、自定义的线程池
        CompletableFuture completableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000L);
                LOGGER.info("使用默认线程池、Runnable的CompletableFuture执行完毕");
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断", e);
                Thread.currentThread().interrupt();
            }
        }, Executors.newFixedThreadPool(10));
        completableFuture2.get();

        // 使用Supplier、自定义的线程池
        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断", e);
                Thread.currentThread().interrupt();
            }
            return "SUCCESS";
        }, Executors.newFixedThreadPool(10));
        LOGGER.info("使用自定义线程池、Supplier的CompletableFuture执行完毕，返回结果为:{}", completableFuture3.get());
    }

    public static void completableFutureTest2() throws ExecutionException, InterruptedException {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            int i = 100/0;
            return 100;
        });
//        future.join();
        future.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        completableFutureTest();
    }
}
