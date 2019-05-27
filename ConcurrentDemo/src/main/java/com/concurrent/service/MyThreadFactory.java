package com.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/27 11:34
 */
public class MyThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyThreadFactory.class);

    int threadCount;
    String factoryName;
    List<String> stats;

    public MyThreadFactory(String factoryName) {
        this.factoryName = factoryName;
        stats = new ArrayList<>();
        threadCount = 1;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread newThread = new Thread(r, this.factoryName + "Thread-" + threadCount);
        threadCount++;
        stats.add(newThread.toString());
        return newThread;
    }

    public void getThreadFactoryStatus() {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = stats.iterator();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        LOGGER.info("当前线程信息:{}", buffer.toString());
    }
}
