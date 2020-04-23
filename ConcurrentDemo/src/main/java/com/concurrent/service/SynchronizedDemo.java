package com.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/5 22:17
 */
public class SynchronizedDemo {

	static char[] c1 = "1234567".toCharArray();

	static char[] c2 = "ABCDEFG".toCharArray();

	private static Object lockObject = new Object();

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedDemo.class);

	public static void main(String[] args) {
		new Thread(() -> {
			for (char c : c1) {
				synchronized (lockObject) {
					System.out.print(c);
					lockObject.notify();
					try {
						lockObject.wait();
					} catch (InterruptedException e) {
						LOGGER.error("进程被打断", e);
						Thread.currentThread().interrupt();
					}
				}
			}
		}).start();


		new Thread(() -> {
			for (char c : c2) {
				synchronized (lockObject) {
					System.out.print(c);
					lockObject.notify();
					try {
						lockObject.wait();
					} catch (InterruptedException e) {
						LOGGER.error("进程被打断", e);
						Thread.currentThread().interrupt();
					}
				}
			}
		}).start();


	}

}
