package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import com.hanlp.constants.CustomsStructuredDataConstant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.util.CollectionUtils;

/**
 * Title: 
 * Description: Bert 模型准确率
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/20 13:31
 */
public class BertAccuracyService {

	private static final String BERT_RESULT = "/Users/wangjie/Downloads/label_test.txt";

	private static final List<Map<String, String>> resultList = new ArrayList<>();

	private static final StringBuilder wordBuilder = new StringBuilder();

	private static final StringBuilder[] correctBuilders = new StringBuilder[8];

	private static final StringBuilder[] guessedBuilders = new StringBuilder[8];

	static {
		for (int i = 0; i < correctBuilders.length; i++) {
			correctBuilders[i] = new StringBuilder();
			guessedBuilders[i] = new StringBuilder();
		}
	}

	public static void transformBertResult() throws IOException {
		List<String> bertResultList = FileUtils.readLines(new File(BERT_RESULT), "UTF-8");

		for (String token : bertResultList) {
			if (StringUtils.isBlank(token)) {
				// 一段话遍历结束，写入文件中
				if (wordBuilder.length() > 0) {
					addSaveListAndClearAppendBuilder();
				}
				continue;
			}
			String[] tokenArray = token.split(" ");
			wordBuilder.append(tokenArray[0]);

			// 人工标注
			if (!Objects.equals("O", tokenArray[1])) {
				if (tokenArray[1].startsWith("B-")) {
					getAppendBuilder(tokenArray[1], true).append(";");
				}
				getAppendBuilder(tokenArray[1], true).append(tokenArray[0]);
			}

			// 模型推测
			if (!Objects.equals("O", tokenArray[2])) {
				if (tokenArray[2].startsWith("B-")) {
					getAppendBuilder(tokenArray[2], false).append(";");
				}
				getAppendBuilder(tokenArray[2], false).append(tokenArray[0]);
			}
		}
	}

	public static void addSaveListAndClearAppendBuilder() {
		for (int i = 0; i < correctBuilders.length; i++) {
			HashMap<String, String> itemMap = new HashMap<>();
			// 人工标注
			if (correctBuilders[i].length() > 0) {
				itemMap.put("mark_result", correctBuilders[i].toString());
				correctBuilders[i].setLength(0);
			}
			// 模型推测
			if (guessedBuilders[i].length() > 0) {
				itemMap.put("ner_result", guessedBuilders[i].toString());
				guessedBuilders[i].setLength(0);
			}
			if (!CollectionUtils.isEmpty(itemMap)) {
				itemMap.put("ner_label", getNerLabelByIndex(i));
				itemMap.put("corpus", wordBuilder.toString());
				resultList.add(itemMap);
			}
		}
		wordBuilder.setLength(0);
	}

	public static String getNerLabelByIndex(int index) {
		switch (index) {
			case 0:
				return CustomsStructuredDataConstant.SeizedOrganization;
			case 1:
				return CustomsStructuredDataConstant.SeizedLocation;
			case 2:
				return CustomsStructuredDataConstant.DeclareGoods;
			case 3:
				return CustomsStructuredDataConstant.RealGoods;
			case 4:
				return CustomsStructuredDataConstant.InvolveCompany;
			case 5:
				return CustomsStructuredDataConstant.InvolveUser;
			case 6:
				return CustomsStructuredDataConstant.StartCountry;
			case 7:
				return CustomsStructuredDataConstant.EndCountry;
			default:
				return CustomsStructuredDataConstant.SeizedOrganization;
		}
	}

	public static StringBuilder getAppendBuilder(String nerLabel, boolean flag) {
		nerLabel = nerLabel.substring(nerLabel.indexOf("-") + 1);
		StringBuilder[] builders = flag ? correctBuilders : guessedBuilders;
		switch (nerLabel) {
			case CustomsStructuredDataConstant.SeizedOrganization:
				return builders[0];
			case CustomsStructuredDataConstant.SeizedLocation:
				return builders[1];
			case CustomsStructuredDataConstant.DeclareGoods:
				return builders[2];
			case CustomsStructuredDataConstant.RealGoods:
				return builders[3];
			case CustomsStructuredDataConstant.InvolveCompany:
				return builders[4];
			case CustomsStructuredDataConstant.InvolveUser:
				return builders[5];
			case CustomsStructuredDataConstant.StartCountry:
				return builders[6];
			case CustomsStructuredDataConstant.EndCountry:
				return builders[7];
			default:
				return builders[0];
		}
	}


	public static void saveBertResult() throws SQLException {
		StringJoiner joiner = new StringJoiner(",",
				"INSERT INTO bert_accuracy(`ner_label`,`corpus`,`mark_result`,`ner_result`) VALUES",
				"");

		resultList.forEach(itemMap -> {
			joiner.add(String.format("('%s','%s','%s','%s')",
					itemMap.get("ner_label"), itemMap.get("corpus"), itemMap.get("mark_result"), itemMap.get("ner_result")));
		});
		DBStoreService.saveOriginData(joiner.toString());
		resultList.clear();
	}

	public static void main(String[] args) throws IOException, SQLException {
		transformBertResult();
		saveBertResult();
	}
}
