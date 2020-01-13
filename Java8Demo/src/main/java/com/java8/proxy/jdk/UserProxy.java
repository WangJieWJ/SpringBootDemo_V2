package com.java8.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-08 09:31
 */
public class UserProxy implements InvocationHandler {


	/**
	 * 被代理的对象
	 */
	private Object target;

	public UserProxy(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(Thread.currentThread().getName() + "执行被代理对象的方法之前");
		Object result = method.invoke(target, args);
		System.out.println(Thread.currentThread().getName() + "执行被代理对象的方法之后");
		return result;
	}
}
