package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-23 20:40
 */
public class CountDownLatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountDownLatchService.class);

    private static void countDownLatchTest() {

        // 创建2个线程的线程池
        Executor executor = Executors.newFixedThreadPool(2);
        int i = 0;
        while (i < 10) {
            // CountDownLatch的计数器是不能循环利用的，
            // 也就是说一旦计数器减到0，再有线程调用await()，该线程会直接通过
            // 所以多次使用需要重复声明
            CountDownLatch latch = new CountDownLatch(2);

            executor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    latch.countDown();
                    LOGGER.info("线程1执行结束！");
                } catch (InterruptedException e) {
                    LOGGER.error("线程被中断", e);
                    Thread.currentThread().interrupt();
                }
            });

            executor.execute(() -> {
                try {
                    Thread.sleep(500L);
                    latch.countDown();
                    LOGGER.info("线程2执行结束！");
                } catch (InterruptedException e) {
                    LOGGER.error("线程被中断", e);
                    Thread.currentThread().interrupt();
                }
            });

            // 等待两个子线程执行完毕
            try {
                latch.await();
            } catch (InterruptedException e) {
                LOGGER.error("线程等待被打断~", e);
                Thread.currentThread().interrupt();
            }
            LOGGER.info("两个线程执行完毕~");
            i++;
            // 业务逻辑：对两个线程的执行结果进行处理
        }
    }

    private static void cyclicBarrierTest() {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            LOGGER.info("回调函数执行成功~ 此处可以用线程池来执行");
        });
        // 创建2个线程的线程池
        Executor executor = Executors.newFixedThreadPool(2);
        int i = 0;
        while (i < 10) {

            executor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    cyclicBarrier.await();
                    LOGGER.info("线程1执行结束！");
                } catch (InterruptedException e) {
                    LOGGER.error("线程被中断", e);
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    LOGGER.error("线程1执行报错!", e);
                }
            });

            executor.execute(() -> {
                try {
                    Thread.sleep(500L);
                    cyclicBarrier.await();
                    LOGGER.info("线程2执行结束！");
                } catch (InterruptedException e) {
                    LOGGER.error("线程被中断", e);
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    LOGGER.error("线程2执行报错!", e);
                }
            });

            i++;
            // 业务逻辑：对两个线程的执行结果进行处理
        }

    }

    public static void main(String[] args) {
//        cyclicBarrierTest();
        countDownLatchTest();
    }
}
