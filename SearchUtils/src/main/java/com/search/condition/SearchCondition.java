package com.search.condition;

import java.util.Arrays;
import java.util.List;

import com.search.enums.Operator;

/**
 * Title: 
 * Description: 基于HyBase的实现
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-30 14:13
 */
public class SearchCondition {

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 字段值
	 */
	private List<String> values;

	/**
	 * 字段关系
	 */
	private Operator operator;

	public SearchCondition(String fieldName, List<String> values, Operator operator) {
		this.fieldName = fieldName;
		this.values = values;
		this.operator = operator;
	}

	@Override
	public String toString() {
		StringBuilder searchBuilder = new StringBuilder();
		switch (this.operator) {
			case Equal:
				searchBuilder.append("(")
						.append(fieldName)
						.append(":(")
						.append(String.join(" OR ", values))
						.append(")) ");
				break;
			case NotEqual:
				searchBuilder.append("NOT (")
						.append(fieldName)
						.append(":(")
						.append(String.join(" OR ", values))
						.append(")) ");
				break;
			default:
		}
		return searchBuilder.toString();
	}

	public static void main(String[] args) {
		SearchCondition searchCondition = new SearchCondition("IR_CONTENT", Arrays.asList("海关", "查获"), Operator.NotEqual);
		System.out.println(searchCondition);
	}
}
