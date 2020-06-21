package com.hanlp.models;

import java.util.List;

import javafx.util.Pair;


/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/3 10:55
 */
public class CustomsStructuredData {

	/**
	 * 查获时间
	 */
	private List<String> SeizedTime;

	/**
	 * 查获组织
	 */
	private List<String> SeizedOrganization;

	/**
	 * 查获地点
	 */
	private List<String> SeizedLocation;

	/**
	 * 涉及个人
	 */
	private List<String> InvolveUser;

	/**
	 * 涉及企业
	 */
	private List<String> InvolveCompany;

	/**
	 * 申报货物
	 */
	private List<String> DeclareGoods;

	/**
	 * 实际货物
	 */
	private List<String> RealGoods;

	/**
	 * 起运港
	 */
	private List<String> StartPort;

	/**
	 * 起运国
	 */
	private List<String> StartCountry;

	/**
	 * 运抵港
	 */
	private List<String> EndPort;

	/**
	 * 运抵国
	 */
	private List<String> EndCountry;

	/**
	 * 数词与量词
	 */
	private List<Pair<String, String>> numeralQuantity;

	/**
	 * 原文内容
	 */
	private String content;

	public List<String> getSeizedTime() {
		return SeizedTime;
	}

	public void setSeizedTime(List<String> seizedTime) {
		SeizedTime = seizedTime;
	}

	public List<String> getSeizedOrganization() {
		return SeizedOrganization;
	}

	public void setSeizedOrganization(List<String> seizedOrganization) {
		SeizedOrganization = seizedOrganization;
	}

	public List<String> getSeizedLocation() {
		return SeizedLocation;
	}

	public void setSeizedLocation(List<String> seizedLocation) {
		SeizedLocation = seizedLocation;
	}

	public List<String> getInvolveUser() {
		return InvolveUser;
	}

	public void setInvolveUser(List<String> involveUser) {
		InvolveUser = involveUser;
	}

	public List<String> getInvolveCompany() {
		return InvolveCompany;
	}

	public void setInvolveCompany(List<String> involveCompany) {
		InvolveCompany = involveCompany;
	}

	public List<String> getDeclareGoods() {
		return DeclareGoods;
	}

	public void setDeclareGoods(List<String> declareGoods) {
		DeclareGoods = declareGoods;
	}

	public List<String> getRealGoods() {
		return RealGoods;
	}

	public void setRealGoods(List<String> realGoods) {
		RealGoods = realGoods;
	}

	public List<Pair<String, String>> getNumeralQuantity() {
		return numeralQuantity;
	}

	public void setNumeralQuantity(List<Pair<String, String>> numeralQuantity) {
		this.numeralQuantity = numeralQuantity;
	}

	public List<String> getStartPort() {
		return StartPort;
	}

	public void setStartPort(List<String> startPort) {
		StartPort = startPort;
	}

	public List<String> getStartCountry() {
		return StartCountry;
	}

	public void setStartCountry(List<String> startCountry) {
		StartCountry = startCountry;
	}

	public List<String> getEndPort() {
		return EndPort;
	}

	public void setEndPort(List<String> endPort) {
		EndPort = endPort;
	}

	public List<String> getEndCountry() {
		return EndCountry;
	}

	public void setEndCountry(List<String> endCountry) {
		EndCountry = endCountry;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CustomsStructuredData{" +
				"SeizedTime=" + SeizedTime +
				", SeizedOrganization=" + SeizedOrganization +
				", SeizedLocation=" + SeizedLocation +
				", InvolveUser=" + InvolveUser +
				", InvolveCompany=" + InvolveCompany +
				", DeclareGoods=" + DeclareGoods +
				", RealGoods=" + RealGoods +
				", StartPort=" + StartPort +
				", StartCountry=" + StartCountry +
				", EndPort=" + EndPort +
				", EndCountry=" + EndCountry +
				", numeralQuantity=" + numeralQuantity +
				", content='" + content + '\'' +
				'}';
	}
}
