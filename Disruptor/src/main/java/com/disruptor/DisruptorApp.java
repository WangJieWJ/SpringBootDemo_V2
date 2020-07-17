package com.disruptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/23 21:44
 */
@SpringBootApplication
public class DisruptorApp {

	public static void main(String[] args) {
		new SpringApplication(DisruptorApp.class).run(args);
	}
}
