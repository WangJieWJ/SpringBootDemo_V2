package com.java8.config;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/5 17:39
 */
public enum TechnologyStack {

    /**
     * 初始化必须放在最前面
     */
    SPRING("2013", "Spring"),
    SPRING_BOOT("2014", "SpringBoot"),
    SPRING_CLOUD("2015", "SpringCloud"),
    DUBBO("2016", "Dubbo");

    private final String year;
    private final String name;

    TechnologyStack(String year, String name) {
        this.year = year;
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public String getName() {
        return name;
    }
}
