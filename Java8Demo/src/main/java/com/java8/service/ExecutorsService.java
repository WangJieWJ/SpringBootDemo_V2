package com.java8.service;

import java.util.concurrent.Executors;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-24 16:36
 */
public class ExecutorsService {

    public static void main(String[] args) {
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(11);
        Executors.newSingleThreadExecutor();
        Executors.newScheduledThreadPool(22);
    }
}
