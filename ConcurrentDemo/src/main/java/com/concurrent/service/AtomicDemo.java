package com.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/3/4 22:36
 */
@Service
public class AtomicDemo {

    private static final AtomicLong tokenNumAtomic = new AtomicLong(0);

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicDemo.class);

    public static void atomicDemo(String userName) {
        try {
            LOGGER.info("用户{}正在进行排队操作,当前队列大小为{}", userName, tokenNumAtomic.get());
            if (tokenNumAtomic.incrementAndGet() > 2) {
                LOGGER.error("用户{}运气不好，碰到并发操作，被堵在门外了！", userName);
                return;
            }
            Thread.sleep(10);
            LOGGER.info("用户{}正在进行登记操作,当前队伍大小为{}", userName, tokenNumAtomic.get());
        } catch (Exception e) {
            LOGGER.error("用户{}运气不好，被强迫出队", userName, e);
        } finally {
            tokenNumAtomic.decrementAndGet();
        }
    }

    public static void main(String[] args) {
        List<String> userNameList = Arrays.asList("赵一", "钱二", "孙三", "李四", "王五", "周六", "吴七", "郑八");
        for (int i = 0; i < 8; i++) {
            final int a = i;
            new Thread(() -> {
                atomicDemo(userNameList.get(a));
            }).start();
        }
    }

}
