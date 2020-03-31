package com.java8.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Title: Java并发编程实战 二刷
 * Description:  测试 编译优化 带来的 有序性问题
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/12 10:39
 */
public class ConcurrencyService {

	/**
	 * 用户名
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ConcurrencyService concurrencyService;

	public static ConcurrencyService getInstance() {
		if (concurrencyService == null) {
			synchronized (ConcurrencyService.class) {
				if (concurrencyService == null) {
					// 1、首先分配一块 内存M
					// 2、在 内存M 上初始化 ConcurrencyService 对象
					// 3、将 内存M 的 地址 赋值给 concurrencyService 变量
					// -------经编译器优化之后-------
					// 1、分配一块内存M
					// 2、将 内存M 的地址 赋值给 concurrencyService 变量
					// 3、最后在 内存M 上进行 初始化变量
					concurrencyService = new ConcurrencyService();
					concurrencyService.setName(Thread.currentThread().getName());
				}
			}
		}
		return concurrencyService;
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Set<Callable<String>> callableSet = new HashSet<>();
		callableSet.add(() -> {
			String result = "";
			try {
				result = getInstance().getName();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		});
		callableSet.add(() -> {
			String result = "";
			try {
				result = getInstance().getName();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		});
		List<Future<String>> futureList = executorService.invokeAll(callableSet);
		futureList.forEach(stringFuture -> {
			try {
				System.out.println(String.format("线程名称:%s，返回值:%s", Thread.currentThread().getName(), stringFuture.get()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
	}
}
