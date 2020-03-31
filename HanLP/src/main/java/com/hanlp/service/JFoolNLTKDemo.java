package com.hanlp.service;

import java.util.Arrays;
import java.util.List;

import me.midday.FoolNLTK;
import me.midday.lexical.AnalysisResult;
import me.midday.lexical.LexicalAnalyzer;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/6 13:48
 */
public class JFoolNLTKDemo {

	public static void main(String[] args){
		LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
		// Participle
		List<String> docs = Arrays.asList(
				"金普海关提醒关注进口危险化学品SDS不合格的风险。近日，金普海关出口加工区B区监管科检验过程中连续发现3批次进口危险化学品不合格情况：报关单号：090420191040042698、报关单号：090420191040043026、报关单号：090420191040043052，不合格原因为现场未随附或丢失中文SDS（Safety Data Sheet，安全数据单）。检验中发现，危险货物SDS不合格的原因主要有：1，SDS未随附货物的物流过程中，而是通过邮件或其他方式传递。2，SDS不规范、数据与分类不匹配。3，SDS资料丢失。",
				"12月4日厦门海关侦办的一起伪报品名走私进口商品案扩案。经查，2017年7月以来，厦门某进出口企业将洋酒、奶粉、化妆品、药品等商品伪报为“洗衣精”“聚丙烯单聚合物”等走私进口销售牟利。经深挖扩线，该案案值由434万元扩至6344万元，涉嫌偷逃税款由117万元扩至1797万元。",
				"四川普通人与川普讲四川普通话"
		);

		// Participle, POS Tagging, Named Entity Recognition
		List<AnalysisResult>  dResults = lexicalAnalyzer.analysis(docs);
		dResults.forEach(System.out::println);
	}
}
