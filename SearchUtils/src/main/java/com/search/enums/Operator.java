package com.search.enums;

/**
 * Title: 
 * Description: 运算枚举类
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-30 13:53
 */
public enum Operator {

	/**
	 * 相等
	 */
	Equal("equal"),
	/**
	 * 不相等
	 */
	NotEqual("notEqual"),
	/**
	 * 范围检索
	 */
	Between("between"),
	/**
	 * 模糊检索
	 */
	Like("like"),
	/**
	 * 模糊排除检索
	 */
	NotLike("notLike"),
	/**
	 * 大于
	 */
	GreaterThan("greaterThan"),
	/**
	 * 大于等于
	 */
	GreaterThanOrEqual("greaterThanOrEqual"),
	/**
	 * 小于
	 */
	LessThan("lessThan"),
	/**
	 * 小于等于
	 */
	LessThanOrEqual("lessThanOrEqual");


	private String desc;

	Operator(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
