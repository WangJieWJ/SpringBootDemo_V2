package com.concurrent.service;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/5 22:06
 */
public class ReentrantLockDemo {

	static char[] c1 = "1234567".toCharArray();

	static char[] c2 = "ABCDEFG".toCharArray();

	public static ReentrantLock reentrantLock = new ReentrantLock();

	public static Condition condition1 = reentrantLock.newCondition();

	public static Condition condition2 = reentrantLock.newCondition();

	public static void main(String[] args) {
		new Thread(() -> {
			for (char c : c1) {
				reentrantLock.lock();
				System.out.print(c);
				condition2.signal();
				try {
					condition1.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				reentrantLock.unlock();
			}
		}).start();

		new Thread(() -> {
			for (char c : c2) {
				reentrantLock.lock();
				System.out.print(c);
				condition1.signal();
				try {
					condition2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reentrantLock.unlock();
			}

		}).start();

	}
}
