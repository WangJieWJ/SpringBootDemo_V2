package com.design.pattern.Proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:36
 */
public class ProxyDemo {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Dog.class);
		enhancer.setCallback(new TimeInterceptor());
		Dog dog = (Dog) enhancer.create();
		dog.eat();
	}
}
