package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hanlp.models.NerInfo;
import com.hanlp.models.NerResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/24 17:30
 */
public class BertAutoNerService {

	private static RestTemplate restTemplate = new RestTemplate();

	private static final String nerUrl = "http://127.0.0.1:9099/encode";

	private static List<String> formatCorpusDataList = Lists.newArrayList();

	private static Integer countNumber = 1;

	private static Integer startIndex = 0;

	private static Integer fileIndex = 1;

	private static StringJoiner joiner = new StringJoiner("\n");

	public static void queryAndFormatCustomsData() throws SQLException {
		String querySql = String.format("SELECT content FROM customs_corpus LIMIT %d,200", (fileIndex - 1) * 200);
		ResultSet resultSet = DBStoreService.findData(querySql);
		while (resultSet.next()) {
			formatCorpusDataList.addAll(Lists.newArrayList(resultSet.getString("content").split("\n")));
		}
		formatCorpusDataList = formatCorpusDataList.stream()
				.map(corpusData -> corpusData.replaceAll("　", "。").replaceAll(" ", "").replaceAll(" ", ""))
				.map(corpusData -> corpusData.replaceAll("\n", "").trim().toLowerCase())
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
	}


	public static void nerCustomsData() {
		for (String customsData : formatCorpusDataList) {
			getNerData(customsData);
		}
	}

	public static void getNerData(String content) {

		Map<String, Object> requestData = new HashMap<>();
		requestData.put("id", System.currentTimeMillis());
		requestData.put("texts", Lists.newArrayList(content));
		requestData.put("is_tokenized", false);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

		ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(nerUrl, HttpMethod.POST, requestEntity, JSONObject.class);
		if (!Objects.equals(responseEntity.getStatusCode(), HttpStatus.OK)) {
			return;
		}
		JSONObject result = responseEntity.getBody();
		if (!Objects.equals(result.getIntValue("status"), 200)) {
			return;
		}
		JSONArray nerResultArray = result.getJSONArray("result");
		if (CollectionUtils.isEmpty(nerResultArray)) {
			return;
		}
		NerResult result1 = getNerResult(nerResultArray.getJSONArray(0));
		for (NerInfo nerInfo : result1.getNerInfos()) {
			joiner.add(String.format("T%d\t%s %d %d\t%s",
					countNumber++, nerInfo.getNerLabelName(),
					startIndex + nerInfo.getStartIndex(),
					startIndex + nerInfo.getEndIndex(),
					nerInfo.getOriginContent()));
		}
		startIndex += (content.length() + 1);
	}

	public static NerResult getNerResult(JSONArray jsonArray) {
		NerResult nerResult = new NerResult();
		NerInfo nerInfo = new NerInfo();
		String content = "";
		String contentLabel = "";
		JSONArray tempArray;
		int startIndex = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			tempArray = jsonArray.getJSONArray(i);
			content = tempArray.getString(0);
			contentLabel = tempArray.getString(1);
			if (!Objects.equals("O", contentLabel)) {
				nerInfo.setStartIndex(startIndex);
				nerInfo.setNerLabelName(contentLabel);
				nerInfo.setOriginContent(content);
				nerInfo.setEndIndex(startIndex + content.length());
				nerResult.addNerInfo(nerInfo);
				nerInfo = new NerInfo();
			}
			startIndex += content.length();
		}
		if (!nerInfo.isEmpty()) {
			nerResult.addNerInfo(nerInfo);
		}
		return nerResult;
	}

	public static void saveAnnDataToFile() throws IOException {
		File file = new File(String.format("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs-v2/customs-data-%d.txt", fileIndex));
		FileUtils.writeLines(file, formatCorpusDataList, "\n", true);

		File annFile = new File(String.format("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs-v2/customs-data-%d.ann", fileIndex));
		FileUtils.write(annFile, joiner.toString(), "UTF-8");
	}

	public static void main(String[] args) throws SQLException, IOException {
		for (int i = 0; i < 22; i++) {
			fileIndex = (i + 1);
			queryAndFormatCustomsData();
			nerCustomsData();
			saveAnnDataToFile();
			formatCorpusDataList.clear();
			countNumber = 1;
			startIndex = 0;
			joiner = new StringJoiner("\n");
		}
	}
}
