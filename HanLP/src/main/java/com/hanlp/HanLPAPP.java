package com.hanlp;

import com.hanlp.service.ABService;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-14 22:43
 */
//@EnableHanLP
@SpringBootApplication
public class HanLPAPP implements ApplicationContextAware {


	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		new SpringApplicationBuilder(HanLPAPP.class).web(true).run(args);
		System.out.println(applicationContext.getBean(ABService.class));
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		HanLPAPP.applicationContext = applicationContext;
	}
}
