//package com.druid.config;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
///**
// * Title:
// * Description: 数据源配置
// * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
// * Company:北京拓尔思信息技术股份有限公司(TRS)
// * Project: SpringBootDemo
// * Author: 王杰
// * Create Time:2019/1/22 16:10
// */
//@Configuration
//public class DruidConfig {
//
//    @Primary
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid")
//    public DataSource dataSourceBuilder(){
//        return DruidDataSourceBuilder.create().build();
//    }
//}
