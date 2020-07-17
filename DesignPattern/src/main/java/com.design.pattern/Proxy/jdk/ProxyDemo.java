package com.design.pattern.Proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:30
 */
public class ProxyDemo {

	public static void main(String[] args) {
		Moveable moveable = (Moveable) Proxy.newProxyInstance(Task.class.getClassLoader(),
				new Class[] { Moveable.class },
				new TimeInvocationHandler(new Task()));
		moveable.move();
	}
}
