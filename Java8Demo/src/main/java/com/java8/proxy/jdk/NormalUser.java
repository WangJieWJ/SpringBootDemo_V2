package com.java8.proxy.jdk;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-08 09:29
 */
public class NormalUser implements User {

	@Override
	public String getUserName() {
		return "NormalUser:";
	}
}
