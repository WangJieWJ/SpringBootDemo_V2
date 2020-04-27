package com.concurrent.service;

import java.util.concurrent.locks.LockSupport;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/5 21:01
 */
public class LockSupportDemo {

	/**
	 * LockSupport
	 *
	 * Synchronized
	 * TransferQueue：容量为0的阻塞队列
	 */


	private static Thread t1;

	private static Thread t2;

	static char[] c1 = "1234567".toCharArray();

	static char[] c2 = "ABCDEFG".toCharArray();


	public static void main(String[] args) {

		t1 = new Thread(() -> {
			for (char c : c1) {
				System.out.print(c);
				LockSupport.unpark(t2);
				LockSupport.park();
			}
		});

		t2 = new Thread(() -> {
			for (char c : c2) {
				LockSupport.park();
				System.out.print(c);
				LockSupport.unpark(t1);
			}
		});
		t1.start();
		t2.start();
	}
}
