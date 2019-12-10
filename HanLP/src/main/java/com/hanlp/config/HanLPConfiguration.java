package com.hanlp.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-21 21:39
 */
@Configuration
public class HanLPConfiguration {

	@PostConstruct
	public void init() {
		System.out.println("此类被注册为SpringBean");
	}

	@Bean(name = "helloJava")
	public String helloWorld() {
		return "Hello,World!";
	}
}
