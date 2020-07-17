package com.disruptor.service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/24 11:28
 */
public class ArrayBlockingQueueDemo {

	/**
	 * 有界堵塞队列
	 */
	public static void testArrayBlockingQueue() {
		ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(16);
		System.out.println("向队列中");
		for (int i = 0; i < 20; i++) {
			System.out.println(i);
			System.out.println(arrayBlockingQueue.offer(i));
		}
	}

	public static void main(String[] args) {

	}
}
