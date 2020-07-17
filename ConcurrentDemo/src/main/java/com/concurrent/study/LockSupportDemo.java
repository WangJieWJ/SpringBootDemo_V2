package com.concurrent.study;

import java.util.concurrent.locks.LockSupport;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/21 21:30
 */
public class LockSupportDemo {

	static Thread t1 = null;

	static Thread t2 = null;

	public static void main(String[] args) {

		t1 = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("1--------" + i);
				LockSupport.unpark(t2);
				LockSupport.park();
			}
		});

		t2 = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				LockSupport.park();
				System.out.println("2--------" + i);
				LockSupport.unpark(t1);
			}
		});


		t1.start();
		t2.start();
	}
}
