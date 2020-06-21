package com.concurrent.study;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/16 20:56
 */
public class BlockingQueueDemo {

	private static String[] numArray = "1,2,3,4,5,6,7".split(",");

	private static String[] charArray = "a,b,c,d,e,f,g".split(",");

	public static void synchronizedDemo() {

		Object lock = new Object();

		new Thread(() -> {
			synchronized (lock) {
				for (int i = 0; i < numArray.length; i++) {
					System.out.print(numArray[i]);
					lock.notify();
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lock.notify();
			}
		}).start();

		new Thread(() -> {
			synchronized (lock) {
				for (int i = 0; i < charArray.length; i++) {
					System.out.print(charArray[i]);
					lock.notify();
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void blockingQueueDemo() {
		BlockingQueue blockingQueue = new SynchronousQueue();
		new Thread(() -> {
			for (int i = 0; i < numArray.length; i++) {
				try {
					blockingQueue.put(numArray[i]);
					System.out.print(blockingQueue.take());
				} catch (InterruptedException e) {

				}
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < charArray.length; i++) {
				try {
					System.out.print(blockingQueue.take());
					blockingQueue.put(charArray[i]);
				} catch (InterruptedException e) {

				}
			}
		}).start();

	}

	public static void main(String[] args) throws InterruptedException {

//		synchronizedDemo();

		blockingQueueDemo();

	}
}
