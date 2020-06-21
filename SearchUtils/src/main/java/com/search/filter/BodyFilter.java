package com.search.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/18 11:12
 */
public class BodyFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(BodyFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("Filter By FilterRegistrationBean 初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LOGGER.info("Filter By FilterRegistrationBean 过滤Before");
		chain.doFilter(request, response);
		LOGGER.info("Filter By FilterRegistrationBean 过滤After");
	}

	@Override
	public void destroy() {
		LOGGER.info("Filter By FilterRegistrationBean 销毁");
	}
}
