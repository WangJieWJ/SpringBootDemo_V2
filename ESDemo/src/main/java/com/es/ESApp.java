package com.es;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Title:
 * Description: ES启动类
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/21 10:35
 */
@SpringBootApplication
public class ESApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ESApp.class).web(true).run(args);
    }
}
