package com.hanlp.dto;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/6 09:44
 */
public class ExportExcelDto {

	/**
	 * 信息渠道
	 */
	private String messageChannel;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 处理是否成功
	 */
	private String success;

	/**
	 * 载体
	 */
	private String carrier;

	/**
	 * 发布时间
	 */
	private String publishTime;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 一级大类
	 */
	private String primaryClass;

	/**
	 * 二级大类
	 */
	private String secondClass;

	/**
	 * 风险详情
	 */
	private String riskContent;

	public String getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(String messageChannel) {
		this.messageChannel = messageChannel;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getRiskContent() {
		return riskContent;
	}

	public void setRiskContent(String riskContent) {
		this.riskContent = riskContent;
	}

	@Override
	public String toString() {
		return "ExportExcelDto{" +
				"messageChannel='" + messageChannel + '\'' +
				", filePath='" + filePath + '\'' +
				", success='" + success + '\'' +
				", carrier='" + carrier + '\'' +
				", publishTime='" + publishTime + '\'' +
				", title='" + title + '\'' +
				", primaryClass='" + primaryClass + '\'' +
				", secondClass='" + secondClass + '\'' +
				", riskContent='" + riskContent + '\'' +
				'}';
	}
}
