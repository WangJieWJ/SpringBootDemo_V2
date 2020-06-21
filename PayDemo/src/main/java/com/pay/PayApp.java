package com.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/28 18:12
 */
@SpringBootApplication
public class PayApp {

	public static void main(String[] args) {
		new SpringApplication(PayApp.class).run(args);
//		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//		applicationContext.register(PayApp.class);
//		applicationContext.refresh();
//		applicationContext.close();
	}
}
