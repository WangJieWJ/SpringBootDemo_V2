package com.design.pattern;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 14:14
 */
@SpringBootApplication
public class DesignPatternApp {

    /**
     * 1、工厂设计模式
     * 2、责任链
     * 3、观察者
     */

    public static void main(String[] args) {
        new SpringApplicationBuilder(DesignPatternApp.class).web(true).run(args);
    }
}
