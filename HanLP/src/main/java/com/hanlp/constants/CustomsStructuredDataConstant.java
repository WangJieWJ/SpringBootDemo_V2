package com.hanlp.constants;

import com.alibaba.fastjson.JSON;
import com.hanlp.models.BratAnnInfo;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/3 11:10
 */
public class CustomsStructuredDataConstant {

	/**
	 * 查获时间
	 */
	public static final String SeizedTime = "SeizedTime";

	/**
	 * 查获单位
	 */
	public static final String SeizedOrganization = "SeizedOrganization";

	/**
	 * 查获地点
	 */
	public static final String SeizedLocation = "SeizedLocation";

	/**
	 * 申报货物
	 */
	public static final String DeclareGoods = "DeclareGoods";

	/**
	 * 实际货物
	 */
	public static final String RealGoods = "RealGoods";

	/**
	 * 起运港
	 */
	public static final String StartPort = "StartPort";

	/**
	 * 起运国
	 */
	public static final String StartCountry = "StartCountry";

	/**
	 * 运抵港
	 */
	public static final String EndPort = "EndPort";

	/**
	 * 运抵国
	 */
	public static final String EndCountry = "EndCountry";

	/**
	 * 数词与量词的合并
	 */
	public static final String NumeralQuantity = "numeralQuantity";

	/**
	 * 涉及企业
	 */
	public static final String InvolveCompany = "InvolveCompany";

	/**
	 * 涉及个人
	 */
	public static final String InvolveUser = "InvolveUser";

	public static void main(String[] args) {
		BratAnnInfo bratAnnInfo = new BratAnnInfo("nerName", "originName", 0, 20);
		System.out.println(JSON.toJSONString(bratAnnInfo));
		JSON.parseObject("{\"endIndex\":20,\"nerName\":\"nerName\",\"originName\":\"originName\",\"startIndex\":0}");
	}
}
