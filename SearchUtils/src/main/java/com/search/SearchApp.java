package com.search;

import com.search.filter.BodyFilter;
import com.search.filter.LanguageFilter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-30 13:50
 */
@SpringBootApplication
@ServletComponentScan
public class SearchApp {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SearchApp.class).web(true).run(args);
	}



	@Bean
	FilterRegistrationBean languageFilterRegistration() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LanguageFilter());
		filterRegistrationBean.setName("languageFilter");
		return filterRegistrationBean;
	}

	@Bean
	FilterRegistrationBean bodyFilterRegistration() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new BodyFilter());
		filterRegistrationBean.setName("bodyFilter");
		return filterRegistrationBean;
	}
}
