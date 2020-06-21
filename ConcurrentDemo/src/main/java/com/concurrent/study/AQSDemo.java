package com.concurrent.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/8 17:05
 */
public class AQSDemo {

	private static List<String> demoList = new ArrayList<>();

	private static final Object lock = new Object();

	private static final Logger LOGGER = LoggerFactory.getLogger(AQSDemo.class);

	public static void synchronizedDemo1() {

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				synchronized (lock) {
					demoList.add(String.valueOf(i));
					lock.notify();
					try {
						lock.wait();
					} catch (InterruptedException e) {
						LOGGER.error("当前线程被打断！", e);
					}
				}
			}
		}).start();

		new Thread(() -> {
			synchronized (lock) {
				if (demoList.size() != 5) {
					try {
						lock.wait();
						System.out.println("当前队列数量达到5");
						lock.notify();
					} catch (InterruptedException e) {
						LOGGER.error("当前线程被打断！", e);
					}
				}

			}
		}).start();
	}

	public static void semaphoreDemo() {
		Semaphore semaphore = new Semaphore(1, true);

		new Thread(() -> {
			try {
				System.out.println("t1启动");
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 10; i++) {

				demoList.add(String.valueOf(i));
				if (demoList.size() == 5) {
					semaphore.release();
					try {
						semaphore.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			semaphore.release();
		}).start();

		new Thread(() -> {
			try {
				System.out.println("t2启动");
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("当前size:" + demoList.size());
			semaphore.release();
		}).start();
	}


	public static void main(String[] args) {
//		synchronizedDemo1();
		semaphoreDemo();
	}
}
