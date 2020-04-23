package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.hanlp.constants.CustomsStructuredDataConstant;
import com.hanlp.models.BratAnnInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/3 17:03
 */
public class CustomsBertService {

	private static AtomicInteger segmentCount = new AtomicInteger(0);

	/**
	 * 训练
	 */
	private static StringBuilder trainCorpusBuilder = new StringBuilder();

	/**
	 * 评测
	 */
	private static StringBuilder devCorpusBuilder = new StringBuilder();

	/**
	 * 验证
	 */
	private static StringBuilder testCorpusBuilder = new StringBuilder();


	private static int[] corpusDataSize = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	public static void createCustomsBertCorpus(String corpusFile) throws IOException {
		List<String> corpusList = FileUtils.readLines(new File(corpusFile), "UTF-8");
		List<BratAnnInfo> bratAnnInfoList = getAnnData(corpusFile.replace(".txt", ".ann"));
		bratAnnInfoList.forEach(bratAnnInfo -> {
			if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.StartCountry)) {
				corpusDataSize[0]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.EndCountry)) {
				corpusDataSize[1]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.InvolveUser)) {
				corpusDataSize[2]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.InvolveCompany)) {
				corpusDataSize[3]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.SeizedOrganization)) {
				corpusDataSize[4]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.SeizedLocation)) {
				corpusDataSize[5]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.DeclareGoods)) {
				corpusDataSize[6]++;
			} else if (Objects.equals(bratAnnInfo.getNerName(), CustomsStructuredDataConstant.RealGoods)) {
				corpusDataSize[7]++;
			}
		});
		int startIndex = 0;
		int endIndex = 0;

		// 做一个大体的划分
		int currentIndex = 0;
		for (String corpusData : corpusList) {

			currentIndex++;

			endIndex = (startIndex + corpusData.length());
			int finalStartIndex = startIndex;
			int finalEndIndex = endIndex;
			final int[] preIndex = { 0 };

			StringBuilder corpusBuilder = (currentIndex < (int) (0.7 * corpusList.size())) ? trainCorpusBuilder :
					((currentIndex < (int) (0.9 * corpusList.size())) ? devCorpusBuilder : testCorpusBuilder);

			AtomicBoolean skipFlag = new AtomicBoolean(true);
			bratAnnInfoList.stream()
					.filter(bratAnnInfo -> bratAnnInfo.getStartIndex() >= finalStartIndex
							&& bratAnnInfo.getEndIndex() <= finalEndIndex)
					.peek(bratAnnInfo -> {
						skipFlag.set(false);
						if (bratAnnInfo.getStartIndex() > finalStartIndex) {
							appendData(corpusBuilder, corpusData.substring(preIndex[0], bratAnnInfo.getStartIndex() - finalStartIndex), "");
						}
						appendData(corpusBuilder, bratAnnInfo.getOriginName(), bratAnnInfo.getNerName());
						preIndex[0] = (bratAnnInfo.getEndIndex() - finalStartIndex);
					}).count();

			startIndex += (corpusData.length() + 1);

			if (skipFlag.get()) {
				continue;
			}
			segmentCount.getAndIncrement();
			appendData(corpusBuilder, corpusData.substring(preIndex[0]), "");
			corpusBuilder.append('\n');
		}

	}

	public static List<BratAnnInfo> getAnnData(String annFilePath) throws IOException {
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

			if (!(Objects.equals(nerArray[0], CustomsStructuredDataConstant.StartCountry)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.EndCountry)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.InvolveUser)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.InvolveCompany)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.DeclareGoods)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.RealGoods)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.SeizedOrganization)
					|| Objects.equals(nerArray[0], CustomsStructuredDataConstant.SeizedLocation))) {
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

	public static void appendData(StringBuilder builder, String word, String pos) {
		if (StringUtils.isNoneBlank(pos)) {
			// 组织
			doAppendData(builder, word, "B-" + pos, "I-" + pos);
		} else {
			String[] wordArray = word.split("。");
			for (String wordItem : wordArray) {
				doAppendData(builder, wordItem, "O", "O");
				builder.append('\n');
			}
			if (!word.endsWith("。")) {
				builder.setLength(builder.length() - 1);
			}
		}

	}

	public static void doAppendData(StringBuilder builder, String word, String prePos, String sufPos) {
		for (int i = 0; i < word.length(); i++) {
			builder.append(String.format("%s %s%n", word.charAt(i), i == 0 ? prePos : sufPos));
		}
	}

	public static void main(String[] args) throws IOException {
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/internal-2019.txt");
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/internal-2020.txt");
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/nationwide-2019-2.txt");
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/nationwide-2019.txt");
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/nationwide-2020.txt");
		createCustomsBertCorpus("/Users/wangjie/Downloads/corpus_30/external-2020.txt");
		FileUtils.writeStringToFile(new File("/Users/wangjie/Downloads/corpus_30/bert/train.txt"), trainCorpusBuilder.toString(), "UTF-8");
		FileUtils.writeStringToFile(new File("/Users/wangjie/Downloads/corpus_30/bert/dev.txt"), devCorpusBuilder.toString(), "UTF-8");
		FileUtils.writeStringToFile(new File("/Users/wangjie/Downloads/corpus_30/bert/test.txt"), testCorpusBuilder.toString(), "UTF-8");
		System.out.println(segmentCount.get());

		System.out.println(String.format("起运国：%s%n运抵国：%s%n涉及个人：%s%n涉及企业：%s%n查获组织：%s%n查获地点：%s%n申报货物：%s%n实际货物：%s",
				corpusDataSize[0], corpusDataSize[1], corpusDataSize[2], corpusDataSize[3],
				corpusDataSize[4], corpusDataSize[5], corpusDataSize[6], corpusDataSize[7]));
	}
}
