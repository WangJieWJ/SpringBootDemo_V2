package com.java8.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import com.java8.proxy.cglib.ActivityService;
import com.java8.proxy.cglib.MyMethodInterceptor;
import com.java8.proxy.jdk.AdminUser;
import com.java8.proxy.jdk.User;
import com.java8.proxy.jdk.UserProxy;

import org.springframework.cglib.proxy.Enhancer;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-08 09:37
 */
public class ProxyTest {

	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

		// JDK------方式一-----
		Class proxyClazz = Proxy.getProxyClass(User.class.getClassLoader(), new Class[] { User.class });
		Constructor constructor = proxyClazz.getConstructor(InvocationHandler.class);
		User adminUser = (User) constructor.newInstance(new UserProxy(new AdminUser()));
		System.out.println(adminUser.getUserName());

		// JDK------方式二-----
		System.out.println("----------------");
		User user = (User) Proxy.newProxyInstance(User.class.getClassLoader(),
				new Class[] { User.class },
				new UserProxy(new AdminUser()));
		System.out.println(user.getUserName());

		// CGLIB---------
		// 通过CGLIB动态代理获取代理对象的过程
		Enhancer enhancer = new Enhancer();
		// 设置enhancer对象的父类
		enhancer.setSuperclass(ActivityService.class);
		// 设置enhancer的回调对象
		enhancer.setCallback(new MyMethodInterceptor());
		// 创建代理对象
		ActivityService proxy = (ActivityService) enhancer.create();
		// 通过代理对象调用目标方法
		proxy.getActivityPrize();
	}
}
