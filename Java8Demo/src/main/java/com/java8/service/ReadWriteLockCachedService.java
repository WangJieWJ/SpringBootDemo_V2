package com.java8.service;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Title:
 * Description: 按需设置缓存
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-20 18:34
 */
public class ReadWriteLockCachedService {

    Object data;
    volatile boolean cacheValid;
    final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    void processCachedData() {
        // 加 读锁
        reentrantReadWriteLock.readLock().lock();

        // 判断数据是否存在

        if (!cacheValid) {
            // 数据不存在，释放read lock
            // 释放 读锁
            reentrantReadWriteLock.readLock().unlock();
            // 加 写锁
            reentrantReadWriteLock.writeLock().lock();
            try {
                // 再次判断是否存在数据
                if (!cacheValid) {
                    // 获取数据，塞入缓存
                }
                // 加 读锁
                reentrantReadWriteLock.readLock().lock();
            } finally {
                // 释放 写锁
                reentrantReadWriteLock.writeLock().unlock();
            }
        }
        // 至此数据已经存在
        try {
            // 业务逻辑

        } finally {
            // 释放 读锁
            reentrantReadWriteLock.readLock().unlock();
        }
    }
}
