package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hanlp.constants.CustomsStructuredDataConstant;
import com.hanlp.models.BratAnnInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/19 14:09
 */
public class NerService {

	private static final String corpusDataPath = "/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/internal-2019.txt";

	private static List<String> corpusDataList;

	private static List<String> annStrList;

	private static List<String> resultList = new ArrayList<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(NerService.class);

	static {
		try {
			corpusDataList = FileUtils.readLines(new File(corpusDataPath), "UTF-8");
			annStrList = FileUtils.readLines(new File(corpusDataPath.replace(".txt", ".ann")), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("语料加载报错！", e);
		}
	}

	public static void createBertCorpusData(String nerLabel) throws IOException {
		int startIndex = 0;
		int endIndex = 0;
		for (String corpusData : corpusDataList) {
			endIndex = startIndex + corpusData.length();

			bertCorpusItem(corpusData, nerLabel, startIndex, endIndex);
			resultList.add("");
			startIndex += (corpusData.length() + 1);
		}
	}

	public static void bertCorpusItem(String content, String nerLabel, int startIndex, int endIndex) throws IOException {
		List<BratAnnInfo> bratAnnInfoList = new ArrayList<>();
		for (String annStr : annStrList) {
			String[] annArray = annStr.split("\t", 3);
			String[] nerArray = annArray[1].split(" ", 3);
			if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.StartPort)) {
				nerArray[0] = CustomsStructuredDataConstant.StartCountry;
			} else if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.EndPort)) {
				nerArray[0] = CustomsStructuredDataConstant.EndCountry;
			}
			if (!Objects.equals(nerArray[0], nerLabel)) {
				continue;
			}
			if (Integer.parseInt(nerArray[1]) < startIndex || Integer.parseInt(nerArray[2]) > endIndex) {
				continue;
			}

			BratAnnInfo bratAnnInfo = new BratAnnInfo(nerArray[0], annArray[2],
					Integer.parseInt(nerArray[1]) - startIndex,
					Integer.parseInt(nerArray[2]) - startIndex);

			bratAnnInfoList.add(bratAnnInfo);
		}
		if (CollectionUtils.isEmpty(bratAnnInfoList)) {
			return;
		}
		// 按照startIndex进行排序
		bratAnnInfoList.sort(BratAnnInfo::compareTo);

		int begin = 0;
		int end;
		for (BratAnnInfo bratAnnInfo : bratAnnInfoList) {
			end = bratAnnInfo.getStartIndex();
			if (end > begin) {
				// 存在未拦截到的部分
				transform(content.substring(begin, end), "O", "");
			}

			transform(bratAnnInfo.getOriginName(), "", nerLabel);

			begin = bratAnnInfo.getEndIndex();
		}
		transform(content.substring(begin), "O", "");
	}

	public static void transform(String content, String label, String nerLabel) {
		content = content.replaceAll("[ 　 ]+", "");
		if (StringUtils.isEmpty(content)) {
			return;
		}
		int contentLenth = content.length();
		if (Objects.equals(label, "O")) {
			for (int i = 0; i < contentLenth; i++) {
				resultList.add(String.format("%s O%n", content.charAt(i)));
			}
		} else {
			for (int i = 0; i < contentLenth; i++) {
				if (i == 0) {
					resultList.add(String.format("%s B-%s%n", content.charAt(i), nerLabel));
					continue;
				}
				resultList.add(String.format("%s I-%s%n", content.charAt(i), nerLabel));
			}
		}
	}


	public static void main(String[] args) throws IOException {
		createBertCorpusData(CustomsStructuredDataConstant.SeizedOrganization);
		File file = new File("/tmp/NERData.txt");
		FileUtils.writeLines(file, resultList, "");
	}
}
