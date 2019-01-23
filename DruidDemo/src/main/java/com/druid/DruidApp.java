package com.druid;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Title:
 * Description: Druid启动类
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 11:01
 */
@SpringBootApplication
public class DruidApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DruidApp.class).web(true).run(args);
    }
}
