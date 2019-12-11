package com.shiro.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 登录校验Filter
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 15:12
 */
public class LoginCheckFilter extends AccessControlFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckFilter.class);

	// 用户未登录跳转地址
	private String unLoginJumpUrl;

	public LoginCheckFilter(String unLoginJumpUrl) {
		this.unLoginJumpUrl = unLoginJumpUrl;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
		LOGGER.info("执行isAccessAllowed方法");
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		LOGGER.info("执行onAccessDenied方法，仅当isAccessAllowed方法返回false的时候才会执行，如果isAccessAllowed方法返回true不会执行该方法");
		LOGGER.info("此处可以判断用户是否登录，如果用户未登录直接跳转到:[{}]", this.unLoginJumpUrl);
		return true;
	}
}
