package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hanlp.constants.CustomsStructuredDataConstant;
import com.hanlp.models.BratAnnInfo;
import org.apache.commons.io.FileUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/31 14:01
 */
public class HanLPAccuracyService {

	private static final RestTemplate restTemplate = new RestTemplate();

	private static List<Map<String, String>> resultList = new ArrayList<>();

	public static void accuracyHanLPNer(String corpusDataPath, String nerTag) throws IOException {

		String dataSource = corpusDataPath.substring(corpusDataPath.lastIndexOf("/") + 1);
		List<BratAnnInfo> bratAnnInfoList = getAnnData(corpusDataPath.replace(".txt", ".ann"), nerTag);

		List<String> corpusDataList = FileUtils.readLines(new File(corpusDataPath), "UTF-8");
		int startIndex = 0;
		int endIndex = 0;
		for (String corpusData : corpusDataList) {
			StringJoiner joiner = new StringJoiner(";");
			endIndex = startIndex + corpusData.length();
			int finalStartIndex = startIndex;
			int finalEndIndex = endIndex;
			bratAnnInfoList.forEach(bratAnnInfo -> {
				if (bratAnnInfo.getStartIndex() > finalStartIndex && bratAnnInfo.getEndIndex() < finalEndIndex) {
					joiner.add(bratAnnInfo.getOriginName());
				}
			});
			if (joiner.length() > 0) {
				Map<String, String> itemMap = new HashMap<>();
				itemMap.put("data_source", dataSource);
				itemMap.put("ner_tag", nerTag);
				itemMap.put("corpus", corpusData);
				itemMap.put("mark_result", joiner.toString());

				itemMap.put("ner_result", getNerResult(corpusData, nerTag));
				resultList.add(itemMap);
			}
			startIndex += (corpusData.length() + 1);
		}
	}

	public static List<BratAnnInfo> getAnnData(String annFilePath, String nerTag) throws IOException {
		List<String> annStrList = FileUtils.readLines(new File(annFilePath), "UTF-8");

		List<BratAnnInfo> bratAnnInfoList = new ArrayList<>();
		for (String annStr : annStrList) {
			String[] annArray = annStr.split("\t", 3);
			String[] nerArray = annArray[1].split(" ", 3);
			if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.StartPort)) {
				nerArray[0] = CustomsStructuredDataConstant.StartCountry;
			} else if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.EndPort)) {
				nerArray[0] = CustomsStructuredDataConstant.EndCountry;
			}
			if (!nerTag.contains(nerArray[0])) {
				continue;
			}

			BratAnnInfo bratAnnInfo = new BratAnnInfo(nerArray[0], annArray[2],
					Integer.parseInt(nerArray[1]),
					Integer.parseInt(nerArray[2]));

			bratAnnInfoList.add(bratAnnInfo);

		}
		// 按照startIndex进行排序
		bratAnnInfoList.sort(BratAnnInfo::compareTo);
		return bratAnnInfoList;
	}

	/**
	 * 实时保存
	 */
	public static void realTimeSave() throws SQLException {
		StringJoiner joiner = new StringJoiner(",", "INSERT INTO hanlp_accuracy(`data_source`,`ner_tag`,`corpus`,`mark_result`,`ner_result`) VALUES", "");

		resultList.forEach(itemMap -> {
			joiner.add(String.format("('%s','%s','%s','%s','%s')",
					itemMap.get("data_source"), itemMap.get("ner_tag"), itemMap.get("corpus"), itemMap.get("mark_result"), itemMap.get("ner_result")));
		});
		DBStoreService.saveOriginData(joiner.toString());
		resultList.clear();
	}

	public static String getNerResult(String originContent, String nerTag) {
		Map<String, Object> param = new HashMap<>();
		param.put("content", originContent);
		ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity("http://localhost:8019/data-etl/ner/analyzer?content={content}", null, JSONObject.class, param);
		JSONObject result = responseEntity.getBody();

		JSONArray jsonArray = result.getJSONArray(String.valueOf(nerTag.charAt(0)).toLowerCase() + nerTag.substring(1));
		if (jsonArray == null || jsonArray.isEmpty()) {
			return "";
		}
		StringJoiner nerResult = new StringJoiner(";");
		for (int i = 0; i < jsonArray.size(); i++) {
			nerResult.add(jsonArray.getString(i));
		}
		return nerResult.toString();
	}

	public static void main(String[] args) throws SQLException {
		List<String> nerList = Arrays.asList(CustomsStructuredDataConstant.SeizedOrganization,
				CustomsStructuredDataConstant.SeizedLocation,
				CustomsStructuredDataConstant.DeclareGoods,
				CustomsStructuredDataConstant.RealGoods,
				CustomsStructuredDataConstant.StartCountry,
				CustomsStructuredDataConstant.EndCountry,
				CustomsStructuredDataConstant.InvolveUser,
				CustomsStructuredDataConstant.InvolveCompany);
		nerList.forEach(nerTag -> {
			try {
				accuracyHanLPNer("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/internal-2019.txt", nerTag);
				accuracyHanLPNer("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/internal-2020.txt", nerTag);
				accuracyHanLPNer("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/nationwide-2019.txt", nerTag);
				accuracyHanLPNer("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/nationwide-2020.txt", nerTag);
				realTimeSave();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
}
