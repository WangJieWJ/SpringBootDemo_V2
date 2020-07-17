package com.design.pattern.Proxy.cglib;

import java.util.Random;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/13 21:34
 */
public class Dog {

	public void eat(){
		System.out.println("Dog eat food");
		try {
			Thread.sleep((new Random()).nextInt(10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
