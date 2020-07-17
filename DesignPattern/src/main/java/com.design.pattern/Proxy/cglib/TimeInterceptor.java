package com.design.pattern.Proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:40
 */
public class TimeInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		System.out.println("开始进行方法调用耗时统计！");
		long startTime = System.currentTimeMillis();
		Object result = proxy.invokeSuper(obj, args);
		System.out.printf("结束方法调用，共耗时%s毫秒%n", (System.currentTimeMillis() - startTime));
		return result;
	}
}
