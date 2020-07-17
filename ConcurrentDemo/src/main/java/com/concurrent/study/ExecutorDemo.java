package com.concurrent.study;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/21 21:49
 */
public class ExecutorDemo {

	/**
	 * FutureTask 测试
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void futureTaskDemo() throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask(() -> {
			Thread.sleep(1000L);
			return "HelloWorld!";
		});
		executorService.submit(futureTask);
		System.out.println(futureTask.get());
	}

	public static void completableFutureDemo(){
//		CompletableFuture completableFuture = CompletableFuture.supplyAsync().;
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
//		futureTaskDemo();

	}
}
