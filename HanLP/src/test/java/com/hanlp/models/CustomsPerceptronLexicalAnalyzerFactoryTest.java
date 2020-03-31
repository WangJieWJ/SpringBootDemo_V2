package com.hanlp.models;

import java.io.IOException;

import com.hanlp.constants.CustomsStructuredDataConstant;
import org.junit.Test;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/9 09:42
 */
public class CustomsPerceptronLexicalAnalyzerFactoryTest {

	/**
	 * 查获时间
	 */
	@Test
	public void createAnalyzerModel4SeizedTime() throws IOException {
//		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.SeizedTime, true, true);
	}

	/**
	 * 查获组织
	 */
	@Test
	public void createAnalyzerModel4SeizedOrganization() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.SeizedOrganization, true, true);
	}

	/**
	 * 查获地点
	 */
	@Test
	public void createAnalyzerModel4SeizedLocation() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.SeizedLocation, true, true);
	}

	/**
	 * 声明货物
	 */
	@Test
	public void createAnalyzerModel4DeclareGoods() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.DeclareGoods, true, true);
	}

	/**
	 * 实际货物
	 */
	@Test
	public void createAnalyzerModel4RealGoods() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.RealGoods, true, true);
	}

	/**
	 * 起运国
	 */
	@Test
	public void createAnalyzerModel4StartCountry() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.StartCountry, true, true);
	}

	/**
	 * 抵运国
	 */
	@Test
	public void createAnalyzerModel4EndCountry() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.EndCountry, true, true);
	}

	/**
	 * 涉及企业
	 */
	@Test
	public void createAnalyzerModel4InvolveCompany() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.InvolveCompany, true, true);
	}

	/**
	 * 涉及个人
	 */
	@Test
	public void createAnalyzerModel4InvolveUser() throws IOException {
		CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(CustomsStructuredDataConstant.InvolveUser, true, true);
	}
}