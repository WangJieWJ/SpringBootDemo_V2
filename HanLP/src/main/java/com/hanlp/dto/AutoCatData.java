package com.hanlp.dto;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-28 09:46
 */
public class AutoCatData {
	/**
	 * 风险详情
	 */
	private String detail;

	/**
	 * 一级大类
	 */
	private String primaryClass;

	/**
	 * 二级大类
	 */
	private String secondClass;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPrimaryClass() {
		return primaryClass;
	}

	public void setPrimaryClass(String primaryClass) {
		this.primaryClass = primaryClass;
	}

	public String getSecondClass() {
		return secondClass;
	}

	public void setSecondClass(String secondClass) {
		this.secondClass = secondClass;
	}
}
