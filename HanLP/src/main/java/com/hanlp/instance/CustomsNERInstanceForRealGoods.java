package com.hanlp.instance;

import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.model.perceptron.feature.FeatureMap;
import com.hankcs.hanlp.model.perceptron.instance.NERInstance;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;

/**
 * Title: 
 * Description: 实际货物<RealGoods> 命名实体 特征提取
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/3 10:17
 */
public class CustomsNERInstanceForRealGoods extends NERInstance {

	public CustomsNERInstanceForRealGoods(String[] wordArray, String[] posArray, String[] nerArray, NERTagSet tagSet, FeatureMap featureMap) {
		super(wordArray, posArray, nerArray, tagSet, featureMap);
	}

	public CustomsNERInstanceForRealGoods(String[] wordArray, String[] posArray, FeatureMap featureMap) {
		super(wordArray, posArray, featureMap);
	}

	@Override
	protected int[] extractFeature(String[] wordArray, String[] posArray, FeatureMap featureMap, int position) {
		List<Integer> featVec = new ArrayList<Integer>();

		String pre4Word = position >= 4 ? wordArray[position - 4] : "_B_";
		String pre3Word = position >= 3 ? wordArray[position - 3] : "_B_";
		String pre2Word = position >= 2 ? wordArray[position - 2] : "_B_";
		String preWord = position >= 1 ? wordArray[position - 1] : "_B_";
		String curWord = wordArray[position];
		String nextWord = position <= wordArray.length - 2 ? wordArray[position + 1] : "_E_";
		String next2Word = position <= wordArray.length - 3 ? wordArray[position + 2] : "_E_";

		String pre4Pos = position > 4 ? posArray[position - 4] : "_B_";
		String pre3Pos = position >= 3 ? posArray[position - 3] : "_B_";
		String pre2Pos = position >= 2 ? posArray[position - 2] : "_B_";
		String prePos = position >= 1 ? posArray[position - 1] : "_B_";
		String curPos = posArray[position];
		String nextPos = position <= posArray.length - 2 ? posArray[position + 1] : "_E_";
		String next2Pos = position <= posArray.length - 3 ? posArray[position + 2] : "_E_";

		StringBuilder sb = new StringBuilder();
		addFeatureThenClear(sb.append(pre4Word).append('1'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre3Word).append('2'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre2Word).append('3'), featVec, featureMap);
		addFeatureThenClear(sb.append(preWord).append('4'), featVec, featureMap);
		addFeatureThenClear(sb.append(curWord).append('5'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextWord).append('6'), featVec, featureMap);
		addFeatureThenClear(sb.append(next2Word).append('7'), featVec, featureMap);

		addFeatureThenClear(sb.append(pre4Pos).append('A'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre3Pos).append('B'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre2Pos).append('C'), featVec, featureMap);
		addFeatureThenClear(sb.append(prePos).append('D'), featVec, featureMap);
		addFeatureThenClear(sb.append(curPos).append('E'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextPos).append('F'), featVec, featureMap);
		addFeatureThenClear(sb.append(next2Pos).append('G'), featVec, featureMap);
		addFeatureThenClear(sb.append(pre2Pos).append(prePos).append('H'), featVec, featureMap);
		addFeatureThenClear(sb.append(prePos).append(curPos).append('I'), featVec, featureMap);
		addFeatureThenClear(sb.append(curPos).append(nextPos).append('O'), featVec, featureMap);
		addFeatureThenClear(sb.append(nextPos).append(next2Pos).append('P'), featVec, featureMap);

		return toFeatureArray(featVec);
	}
}
