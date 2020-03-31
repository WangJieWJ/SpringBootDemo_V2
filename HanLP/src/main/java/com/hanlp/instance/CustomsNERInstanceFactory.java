package com.hanlp.instance;

import java.io.IOException;
import java.util.List;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.model.perceptron.NERTrainer;
import com.hankcs.hanlp.model.perceptron.feature.FeatureMap;
import com.hankcs.hanlp.model.perceptron.instance.Instance;
import com.hankcs.hanlp.model.perceptron.instance.NERInstance;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;
import com.hankcs.hanlp.model.perceptron.utility.Utility;
import com.hanlp.constants.CustomsStructuredDataConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/1 21:08
 */
public class CustomsNERInstanceFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomsNERInstanceFactory.class);

	public static NERInstance getNERInstance(String nerLabel, String[] wordArray, String[] posArray, String[] nerArray, NERTagSet tagSet, FeatureMap featureMap) {
		switch (nerLabel) {
			case CustomsStructuredDataConstant.SeizedOrganization:
				return new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.SeizedLocation:
				return new CustomsNERInstanceForSeizedLocation(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.DeclareGoods:
				return new CustomsNERInstanceForDeclareGoods(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.RealGoods:
				return new CustomsNERInstanceForRealGoods(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.StartCountry:
				return new CustomsNERInstanceForStartCountry(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.EndCountry:
				return new CustomsNERInstanceForEndCountry(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.InvolveCompany:
				return new CustomsNERInstanceForInvolveCompany(wordArray, posArray, nerArray, tagSet, featureMap);
			case CustomsStructuredDataConstant.InvolveUser:
				return new CustomsNERInstanceForInvolveUser(wordArray, posArray, nerArray, tagSet, featureMap);
			default:
				LOGGER.error("未识别的命名实体！{}", nerLabel);
				return null;
		}
	}

	public static NERInstance getNERInstance(String nerLabel, String[] wordArray, String[] posArray, FeatureMap featureMap) {
		switch (nerLabel) {
			case CustomsStructuredDataConstant.SeizedOrganization:
				return new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.SeizedLocation:
				return new CustomsNERInstanceForSeizedLocation(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.DeclareGoods:
				return new CustomsNERInstanceForDeclareGoods(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.RealGoods:
				return new CustomsNERInstanceForRealGoods(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.StartCountry:
				return new CustomsNERInstanceForStartCountry(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.EndCountry:
				return new CustomsNERInstanceForEndCountry(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.InvolveCompany:
				return new CustomsNERInstanceForInvolveCompany(wordArray, posArray, featureMap);
			case CustomsStructuredDataConstant.InvolveUser:
				return new CustomsNERInstanceForInvolveUser(wordArray, posArray, featureMap);
			default:
				LOGGER.error("未识别的命名实体！{}", nerLabel);
				return null;
		}
	}


	/**
	 * 获取命名实体识别的 在线学习模型
	 * @param trainingFile 训练文件
	 * @param nerLabel 识别的实体名称
	 * @param modelPath 模型存储路径
	 */
	public static LinearModel getNerTrainLinearModel(String trainingFile, String nerLabel, String modelPath) throws IOException {
		NERTrainer nerTrainer = new NERTrainer() {
			@Override
			protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
				NERTagSet tagSet = (NERTagSet) featureMap.tagSet;
				List<String[]> collector = Utility.convertSentenceToNER(sentence, tagSet);
				String[] wordArray = new String[collector.size()];
				String[] posArray = new String[collector.size()];
				String[] nerArray = new String[collector.size()];
				Utility.reshapeNER(collector, wordArray, posArray, nerArray);
				switch (nerLabel) {
					case CustomsStructuredDataConstant.SeizedOrganization:
						return new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.SeizedLocation:
						return new CustomsNERInstanceForSeizedLocation(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.DeclareGoods:
						return new CustomsNERInstanceForDeclareGoods(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.RealGoods:
						return new CustomsNERInstanceForRealGoods(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.StartCountry:
						return new CustomsNERInstanceForStartCountry(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.EndCountry:
						return new CustomsNERInstanceForEndCountry(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.InvolveCompany:
						return new CustomsNERInstanceForInvolveCompany(wordArray, posArray, nerArray, tagSet, featureMap);
					case CustomsStructuredDataConstant.InvolveUser:
						return new CustomsNERInstanceForInvolveUser(wordArray, posArray, nerArray, tagSet, featureMap);
					default:
						LOGGER.error("未识别的命名实体！{}", nerLabel);
						return null;
				}
			}
		};
		nerTrainer.tagSet.nerLabels.clear();
		nerTrainer.tagSet.nerLabels.add(nerLabel);
		return nerTrainer.train(trainingFile, null, modelPath, 0, 100, Runtime.getRuntime().availableProcessors()).getModel();
	}

}
