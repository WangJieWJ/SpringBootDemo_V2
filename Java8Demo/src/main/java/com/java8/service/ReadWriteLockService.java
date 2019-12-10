package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-20 17:50
 */
public class ReadWriteLockService {

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = reentrantReadWriteLock.readLock();
    private final Lock writeLock = reentrantReadWriteLock.writeLock();

    private final Map<String, String> cacheData = new TreeMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteLockService.class);

    /**
     * 获取对应Key的值
     *
     * @param key 对应key
     */
    public String get(String key) {
        readLock.lock();
        try {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cacheData.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 获取所有缓存Key
     */
    public String[] allKeys() {
        readLock.lock();
        try {
            return cacheData.keySet().toArray(new String[0]);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 设置缓存
     *
     * @param key   具体key
     * @param value 具体的值
     */
    public String put(String key, String value) {
        writeLock.lock();
        try {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cacheData.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清除缓存数据
     */
    public void clear() {
        writeLock.lock();
        try {
            cacheData.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockService readWriteLockService = new ReadWriteLockService();
        Thread writeThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                final int item = i;
                new Thread(() -> {
                    readWriteLockService.put(String.format("key_%s", item),
                            String.format("value_%s", item));
                    LOGGER.info("成功写入缓存值为:{}", String.format("key_%s", item));
                }).start();
            }
        });

        Thread readThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                final int item = i;
                new Thread(() -> {
                    LOGGER.info("成功读取缓存值为:{}", readWriteLockService.get(String.format("key_%s", item)));
                }).start();
            }
        });

        writeThread.start();
        readThread.start();
        Thread.sleep(10000000L);
    }

}
