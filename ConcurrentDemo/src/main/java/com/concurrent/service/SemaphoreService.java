package com.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Title:
 * Description: Semaphore当前在多线程环境下被扩放使用，操作系统的信号量是个很重要的概念，在进程控制方面都有应用
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/3/4 22:11
 */
@Service
public class SemaphoreService {

    private static final Semaphore semaphore = new Semaphore(1);

    private static final Logger LOGGER = LoggerFactory.getLogger(SemaphoreService.class);

    public static void semaphoreDemo(String userName, String productName) {
        try {
            LOGGER.info("{}准备开始获得主动权，当前可获得的并发数为{}", userName, semaphore.availablePermits());
            semaphore.acquire();
            LOGGER.info("{}获得了主动权，开始操作产品{}", userName, productName);
            Thread.sleep(1000L);

        } catch (Exception e) {
            LOGGER.error("用户并发出现问题！", e);
        } finally {
            semaphore.release();
        }
    }


    public static void main(String[] args) {
        List<String> userNameList = Arrays.asList("赵一", "钱二", "孙三", "李四", "王五", "周六", "吴七", "郑八");
        List<String> productNameList = Arrays.asList("衣服", "鞋子", "包", "帽子", "短袖", "围巾", "羽绒服", "衬衫");
        for (int i = 0; i < 8; i++) {
            final int a = i;
            new Thread(() -> {
                semaphoreDemo(userNameList.get(a), productNameList.get(a));
            }).start();
        }
    }
}
