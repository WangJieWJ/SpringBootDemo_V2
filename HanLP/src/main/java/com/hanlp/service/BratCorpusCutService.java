package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hanlp.models.BratAnnInfo;
import org.apache.commons.io.FileUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/23 14:56
 */
public class BratCorpusCutService {

	public static void fileCut(String fileName) throws IOException {
		List<BratAnnInfo> bratAnnInfoList = CustomsBertService.getAnnData(fileName.replace(".txt", ".ann"));
		List<String> corpusDataList = FileUtils.readLines(new File(fileName), "UTF-8");
		int[] startAndEndIndex = new int[] { 0, 0, 1 };
		int count = 0;
		StringBuilder builder = new StringBuilder();
		StringBuilder annBuilder;
		for (String corpusData : corpusDataList) {
			count++;
			builder.append(corpusData).append("\n");
			startAndEndIndex[1] += (corpusData.length() + 1);
			if (count % 100 == 0) {
				annBuilder = getAnnData(bratAnnInfoList, startAndEndIndex);
				saveDataToNewFile(builder, annBuilder, fileName, count / 100);
				startAndEndIndex[0] = startAndEndIndex[1];
			}
		}
		startAndEndIndex[1] = Integer.MAX_VALUE;
		annBuilder = getAnnData(bratAnnInfoList, startAndEndIndex);
		saveDataToNewFile(builder, annBuilder, fileName, (count / 100) + 1);
	}

	public static StringBuilder getAnnData(List<BratAnnInfo> bratAnnInfoList, int[] startAndEndIndex) {
		StringBuilder annBuilder = new StringBuilder();
		bratAnnInfoList.forEach(bratAnnInfo -> {
			if (bratAnnInfo.getStartIndex() >= startAndEndIndex[0]
					&& bratAnnInfo.getEndIndex() <= startAndEndIndex[1]) {
				annBuilder.append(String.format("T%d\t%s %d %d\t%s",
						startAndEndIndex[2]++, bratAnnInfo.getNerName(),
						bratAnnInfo.getStartIndex() - startAndEndIndex[0], bratAnnInfo.getEndIndex() - startAndEndIndex[0],
						bratAnnInfo.getOriginName()))
						.append("\n");
			}
		});
		return annBuilder;
	}

	public static void saveDataToNewFile(StringBuilder builder, StringBuilder annBuilder, String fileName, int fileIndex) throws IOException {
		FileUtils.writeStringToFile(new File(fileName.replace(".txt", String.format("-%d.txt", fileIndex))),
				builder.toString(), "UTF-8");
		FileUtils.writeStringToFile(new File(fileName.replace(".txt", String.format("-%d.ann", fileIndex))),
				annBuilder.toString(), "UTF-8");
		builder.setLength(0);
		annBuilder.setLength(0);
	}

	public static void main(String[] args) throws IOException {
		fileCut("/Users/wangjie/Downloads/external-2020.txt");
		fileCut("/Users/wangjie/Downloads/internal-2019.txt");
		fileCut("/Users/wangjie/Downloads/internal-2020.txt");
		fileCut("/Users/wangjie/Downloads/nationwide-2019-sub.txt");
		fileCut("/Users/wangjie/Downloads/nationwide-2019.txt");
		fileCut("/Users/wangjie/Downloads/nationwide-2020.txt");
	}
}
