package com.java8.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-29 11:12
 */
public class GuardedObject<T> {

    /**
     * 受保护的对象
     */
    T obj;
    final Lock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();
    /**
     * 超时时间
     */
    final int timeout = 2;
    /**
     * 保存所有的GuardedObject
     */
    static final Map<Object, GuardedObject> gos = new ConcurrentHashMap<>();

    /**
     * 静态方法创建GuardedObject
     *
     * @param key 唯一标识
     * @param <K> 类型
     * @return
     */
    static <K> GuardedObject create(K key) {
        GuardedObject go = new GuardedObject();
        gos.put(key, go);
        return go;
    }

    /**
     * 唤醒等待的线程
     *
     * @param key 唯一标识
     * @param obj 等待的结果
     * @param <K>
     * @param <T>
     */
    static <K, T> void fireEvent(K key, T obj) {
        GuardedObject go = gos.remove(key);
        if (go != null) {
            go.onChanged(obj);
        }
    }

    /**
     * 获取受保护对象
     *
     * @param p 竞态条件，可以自己写
     */
    T get(Predicate<T> p) {
        lock.lock();
        try {
            //MESA管程推荐写法
            while (!p.test(obj)) {
                condition.await(timeout, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        //返回非空的受保护对象
        return obj;
    }

    /**
     * 事件通知方法
     *
     * @param obj 等待的结果
     */
    void onChanged(T obj) {
        lock.lock();
        try {
            this.obj = obj;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }


}
