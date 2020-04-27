package com.java8.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/23 08:26
 */
@Aspect
@Component
public class AspectDemo {

	private static final Logger LOGGER = LoggerFactory.getLogger(AspectDemo.class);

	@Before("execution(* saveUser(..))")
	public void saveUserLog() {
		LOGGER.info("保存用户信息之前，进行数据准备工作……saveUserLog");
	}

	@After("execution(* com.java8.aspect.UserService.*(..))")
	public void userOperation() {
		LOGGER.info("刚操作完用户的信息……userOperation");
	}

	@Before("execution(* updateUserInfo(..))")
	public void updateUserInfoLog() {
		LOGGER.info("对用户进行修改操作之前，进行数据备份工作……updateUserInfoLog");
	}

	@Around("execution(* com.java8.aspect.UserService.deleteUser(..))")
	public void deleteUserLog(ProceedingJoinPoint proceedingJoinPoint) {
		try {
			LOGGER.info("删除用户信息之前，进行用户数据备份……deleteUserLog");
			proceedingJoinPoint.proceed();
			LOGGER.info("删除用户信息之后，进行用户数据备份……deleteUserLog");
		} catch (Throwable throwable) {
			LOGGER.error("删除用户出错……deleteUserLog", throwable);
		}
	}
}
