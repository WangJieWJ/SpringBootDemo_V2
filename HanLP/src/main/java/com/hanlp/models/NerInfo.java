package com.hanlp.models;

import java.io.Serializable;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/26 11:07
 */
public class NerInfo implements Serializable {


	/**
	 * 识别到的标签名称
	 */
	private String nerLabelName;

	/**
	 * 识别到的原文
	 */
	private String originContent;

	/**
	 * 开始索引
	 */
	private int startIndex;

	/**
	 * 结束索引
	 */
	private int endIndex;


	/**
	 * 是否为空
	 */
	private boolean empty = true;

	public String getNerLabelName() {
		return nerLabelName;
	}

	public void setNerLabelName(String nerLabelName) {
		this.nerLabelName = nerLabelName;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		empty = false;
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getOriginContent() {
		return originContent;
	}

	public void setOriginContent(String originContent) {
		this.originContent = originContent;
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public String toString() {
		return "NerInfo{" +
				"nerLabelName='" + nerLabelName + '\'' +
				", originContent='" + originContent + '\'' +
				", startIndex=" + startIndex +
				", endIndex=" + endIndex +
				'}';
	}
}
