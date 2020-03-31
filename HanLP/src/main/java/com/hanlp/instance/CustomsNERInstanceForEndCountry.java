package com.hanlp.instance;

import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.model.perceptron.feature.FeatureMap;
import com.hankcs.hanlp.model.perceptron.instance.NERInstance;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;

/**
 * Title: 
 * Description: 运抵港<EndPort>、运抵国<EndCountry> 命名实体 特征提取
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/3 15:45
 */
public class CustomsNERInstanceForEndCountry extends NERInstance {

	public CustomsNERInstanceForEndCountry(String[] wordArray, String[] posArray, String[] nerArray, NERTagSet tagSet, FeatureMap featureMap) {
		super(wordArray, posArray, nerArray, tagSet, featureMap);
	}

	public CustomsNERInstanceForEndCountry(String[] wordArray, String[] posArray, FeatureMap featureMap) {
		super(wordArray, posArray, featureMap);
	}

	@Override
	protected int[] extractFeature(String[] wordArray, String[] posArray, FeatureMap featureMap, int position) {
		List<Integer> featVec = new ArrayList<Integer>();

		String pre2Word = position >= 2 ? wordArray[position - 2] : "_B_";
		String preWord = position >= 1 ? wordArray[position - 1] : "_B_";
		String curWord = wordArray[position];
		String nextWord = position <= wordArray.length - 2 ? wordArray[position + 1] : "_E_";
		String next2Word = position <= wordArray.length - 3 ? wordArray[position + 2] : "_E_";

		String pre2Pos = position >= 2 ? posArray[position - 2] : "_B_";
		String prePos = position >= 1 ? posArray[position - 1] : "_B_";
		String curPos = posArray[position];
		String nextPos = position <= posArray.length - 2 ? posArray[position + 1] : "_E_";
		String next2Pos = position <= posArray.length - 3 ? posArray[position + 2] : "_E_";

		StringBuilder sb = new StringBuilder();
		addFeatureThenClear(sb.append(pre2Word).append('1'), featVec, featureMap);
		addFeatureThenClear(sb.append(preWord).append('2'), featVec, featureMap);
		addFeatureThenClear(sb.append(curWord).append('3'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextWord).append('4'), featVec, featureMap);
		addFeatureThenClear(sb.append(next2Word).append('5'), featVec, featureMap);

		addFeatureThenClear(sb.append(pre2Pos).append('A'), featVec, featureMap);
		addFeatureThenClear(sb.append(prePos).append('B'), featVec, featureMap);
		addFeatureThenClear(sb.append(curPos).append('C'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextPos).append('D'), featVec, featureMap);
		addFeatureThenClear(sb.append(next2Pos).append('E'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre2Pos).append(prePos).append('F'), featVec, featureMap);
		addFeatureThenClear(sb.append(prePos).append(curPos).append('G'), featVec, featureMap);
		addFeatureThenClear(sb.append(curPos).append(nextPos).append('H'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextPos).append(next2Pos).append('I'), featVec, featureMap);

		return toFeatureArray(featVec);
	}
}
