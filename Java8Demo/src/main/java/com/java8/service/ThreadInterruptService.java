package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-19 13:29
 */
public class ThreadInterruptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadInterruptService.class);

    public static class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                LOGGER.info("线程还在执行!");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread workerThread = new Thread(new Worker());
        workerThread.start();

        Thread.sleep(200);
        workerThread.interrupt();

        workerThread.join();
    }
}
