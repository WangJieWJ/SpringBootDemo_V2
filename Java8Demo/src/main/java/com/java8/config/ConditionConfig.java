package com.java8.config;

import com.java8.condition.LinuxCondition;
import com.java8.condition.MacCondition;
import com.java8.condition.WindowsCondition;
import com.java8.service.LinuxListService;
import com.java8.service.ListService;
import com.java8.service.MacListService;
import com.java8.service.WindowsListService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-05 15:52
 */
@Configuration
public class ConditionConfig {

	/**
	 * 通过 @Conditional 注解，实现符合 WindowsCondition 条件，则实例化 WindowsListService
	 */
	@Bean
	@Conditional(WindowsCondition.class)
	public ListService windowsListService() {
		return new WindowsListService();
	}

	/**
	 * 通过 @Conditional 注解，实现符合 LinuxCondition 条件，则实例化 LinuxListService
	 */
	@Bean
	@Conditional(LinuxCondition.class)
	public ListService linuxListService() {
		return new LinuxListService();
	}

	/**
	 * 通过 @Conditional 注解，实现符合 MacCondition 条件，则实例化 MacListService
	 */
	@Bean
	@Conditional(MacCondition.class)
	public ListService macListService() {
		return new MacListService();
	}
}
