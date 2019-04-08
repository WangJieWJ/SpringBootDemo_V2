package com.concurrent;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/3/4 22:09
 */
@SpringBootApplication
public class ConcurrentApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConcurrentApp.class).web(true).run(args);
    }
}
