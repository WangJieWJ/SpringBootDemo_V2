package com.design.pattern.Proxy.jdk;

import java.util.Random;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:12
 */
public class Task implements Moveable {

	@Override
	public void move() {
		try {
			System.out.println("正在移动任务！");
			Thread.sleep((new Random()).nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
