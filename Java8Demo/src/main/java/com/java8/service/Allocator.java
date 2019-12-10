package com.java8.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-18 17:37
 */
public class Allocator {

    private List<Object> als = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Allocator.class);

    /**
     * 一次性申请所有资源
     *
     * @param from 源
     * @param to   目标
     */
    synchronized void apply(Object from, Object to) {
        while (als.contains(from) || als.contains(to)) {
            try {
                wait();
            } catch (InterruptedException e) {
                LOGGER.error("线程进入等待状态失败!", e);
            }
        }
        als.add(from);
        als.add(to);
    }

    /**
     * 归还资源
     *
     * @param from 源
     * @param to   目标
     */
    synchronized void free(Object from, Object to) {
        als.remove(from);
        als.remove(to);
        /**
         * 尽量使用notifyAll()
         * notify()是会随机地通知等待队列中的一个线程，而notifyAll()会通知等待队列中的所有线程
         * 假设我们有资源A、B、C、D，线程1申请到了AB，线程2申请到了CD，
         * 此时线程3申请AB，会进入等待队列（AB分配给线程1，线程3要求的条件不满足），
         * 线程4申请CD也会进入等待队列。我们再假设之后线程1归还了资源AB，如果使用notify()来通知等待队列中的线程，
         * 有可能被通知的是线程4，但线程4申请的是CD，所以此时线程4还是会继续等待，而真正该唤醒的线程3就再也没有机会被唤醒了
         */
        notifyAll();
    }
}
