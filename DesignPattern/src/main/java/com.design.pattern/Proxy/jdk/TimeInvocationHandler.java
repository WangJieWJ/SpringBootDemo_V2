package com.design.pattern.Proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:14
 */
public class TimeInvocationHandler implements InvocationHandler {

	private Task task;

	public TimeInvocationHandler(Task task) {
		this.task = task;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("开始进行方法调用耗时统计！");
		long startTime = System.currentTimeMillis();
		Object returnObject = method.invoke(task, args);
		System.out.printf("结束方法调用，共耗时%s毫秒%n", (System.currentTimeMillis() - startTime));
		return returnObject;
	}
}
