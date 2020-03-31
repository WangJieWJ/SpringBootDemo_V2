package com.hanlp.nerecognizer;

import java.io.IOException;
import java.util.List;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.model.perceptron.PerceptronNERecognizer;
import com.hankcs.hanlp.model.perceptron.feature.FeatureMap;
import com.hankcs.hanlp.model.perceptron.instance.Instance;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;
import com.hankcs.hanlp.model.perceptron.utility.Utility;
import com.hanlp.instance.CustomsNERInstanceFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/1 20:59
 */
public class CustomsPerceptronNERecognizer extends PerceptronNERecognizer {

	private String nerLabel;

	public CustomsPerceptronNERecognizer(String nerLabel, LinearModel nerModel) {
		super(nerModel);
		this.nerLabel = nerLabel;
	}

	public CustomsPerceptronNERecognizer(String nerLabel, String nerModelPath) throws IOException {
		super(nerModelPath);
		this.nerLabel = nerLabel;
	}

	@Override
	public boolean learn(String segmentedTaggedNERSentence) {
		return super.learn(createInstance(Sentence.create(segmentedTaggedNERSentence), model.featureMap));
	}

	@Override
	protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
		NERTagSet tagSet = (NERTagSet) featureMap.tagSet;
		List<String[]> collector = Utility.convertSentenceToNER(sentence, tagSet);
		String[] wordArray = new String[collector.size()];
		String[] posArray = new String[collector.size()];
		String[] nerArray = new String[collector.size()];
		Utility.reshapeNER(collector, wordArray, posArray, nerArray);
		return CustomsNERInstanceFactory.getNERInstance(nerLabel, wordArray, posArray, nerArray, tagSet, featureMap);
	}

	@Override
	public String[] recognize(String[] wordArray, String[] posArray) {
		return super.recognize(CustomsNERInstanceFactory.getNERInstance(nerLabel, wordArray, posArray, model.featureMap));
	}

}
