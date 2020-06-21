package com.hanlp.models;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronNERecognizer;
import com.hankcs.hanlp.model.perceptron.PerceptronPOSTagger;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import com.hanlp.constants.CustomsStructuredDataConstant;
import com.hanlp.instance.CustomsNERInstanceFactory;
import com.hanlp.instance.CustomsPOSFactory;
import com.hanlp.nerecognizer.CustomsPerceptronNERecognizer;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/2/28 10:45
 */
public class CustomsPerceptronLexicalAnalyzerFactory {

	public static Map<String, PerceptronLexicalAnalyzer> analyzerCacheMap = new HashMap<>();


	/**
	 * 获取结构化感知器标注模型
	 * @param nerLabel 要识别的命名实体
	 * @param retrainPOS 是否重新训练POS
	 * @param retrainNER 是否重新训练NER
	 */
	public static PerceptronLexicalAnalyzer getPerceptronLexicalAnalyzer(String nerLabel, boolean retrainPOS, boolean retrainNER) throws IOException {
		if (analyzerCacheMap.containsKey(nerLabel)) {
			return analyzerCacheMap.get(nerLabel);
		}
		String trainingFile = String.format("/Users/wangjie/Development/项目/海关/二期结构化处理/perceptron_%s.txt", nerLabel);
		String modelPath = String.format("/Users/wangjie/Development/项目/海关/二期结构化处理/perceptron_%s_ner.bin", nerLabel);
		// modelPath = "/Users/wangjie/Development/项目/海关/二期结构化处理/比较好的模型/perceptron_SeizedOrganization_ner.bin";
		// 1、训练一个分词器(因为语料库就是根据默认感知器分词器生成的，所以此处直接默认的分词语料)

		String cwsModelFile = "/Users/wangjie/Development/ELK/hanlp/data/model/perceptron/large/cws.bin";
		PerceptronSegmenter perceptronSegmenter = new PerceptronSegmenter(cwsModelFile);

		// 2、训练一个词性标注
		String posModelFile = "/Users/wangjie/Development/ELK/hanlp/data/model/perceptron/pku1998/pos.bin";
		PerceptronPOSTagger perceptronPOSTagger = retrainPOS ? new PerceptronPOSTagger(CustomsPOSFactory.getPOSLinearModel(trainingFile, modelPath.replace("ner.bin", "pos.bin"))) : new PerceptronPOSTagger(posModelFile);

		// 3、训练命名实体的
		PerceptronNERecognizer recognizer = retrainNER ? new CustomsPerceptronNERecognizer(nerLabel, CustomsNERInstanceFactory.getNerTrainLinearModel(trainingFile, nerLabel, modelPath)) : new CustomsPerceptronNERecognizer(nerLabel, modelPath);

		// 4、进行结构化感知器标注框架构建
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = new PerceptronLexicalAnalyzer(perceptronSegmenter, perceptronPOSTagger, recognizer);
		analyzerCacheMap.put(nerLabel, perceptronLexicalAnalyzer);
		return perceptronLexicalAnalyzer;
	}

	/**
	 * 模型保存
	 * @param nerLabel 要保存的命名实体
	 * @param POSModelSave 是否保存词性标注的模型
	 * @param NERModelSave 是否保存命名实体的模型
	 */
	public static JSONObject savePerceptronLexicalAnalyzerModel(String nerLabel, boolean POSModelSave, boolean NERModelSave) throws IOException {
		JSONObject modelSaveResult = new JSONObject();
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = analyzerCacheMap.get(nerLabel);
		if (Objects.isNull(perceptronLexicalAnalyzer)) {
			modelSaveResult.put("modelExist", "perceptronLexicalAnalyzer 不存在！");
			return modelSaveResult;
		}
		modelSaveResult.put("modelExist", "perceptronLexicalAnalyzer 模型可以保存");
		String modelFilePath = "/tmp/hanlp";
		if (POSModelSave) {
			perceptronLexicalAnalyzer.getPerceptronPOSTagger().getModel().save(String.format("%s/%s_pos.bin", modelFilePath, nerLabel));
			modelSaveResult.put("posModel", "词性标注模型保存成功！");
		}
		if (NERModelSave) {
			perceptronLexicalAnalyzer.getPerceptionNERecognizer().getModel().save(String.format("%s/%s_ner.bin", modelFilePath, nerLabel));
			modelSaveResult.put("nerModel", "命名实体识别模型保存成功！");
		}
		return modelSaveResult;
	}

	/**
	 * 获取结构化之后的数据
	 * @param content 文本内容
	 * @return
	 * @throws IOException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static CustomsStructuredData getCustomsStructuredData(String content) throws IOException, NoSuchFieldException, IllegalAccessException {
		CustomsStructuredData customsStructuredData = new CustomsStructuredData();
		customsStructuredData.setContent(content);
		setNerDataWithOtherField(customsStructuredData, CustomsStructuredDataConstant.SeizedOrganization, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.SeizedLocation, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.DeclareGoods, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.RealGoods, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.StartCountry, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.EndCountry, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.InvolveUser, false, false);
		setNerData(customsStructuredData, CustomsStructuredDataConstant.InvolveCompany, false, false);

		return customsStructuredData;
	}


	/**
	 * 设置具体命名实体的识别结果
	 * @param customsStructuredData 识别结果
	 * @param nerLabel 命名实体
	 * @param retrainPOS 是否需要重新训练POS
	 * @param retrainNER 是否需要重新训练NER
	 * @throws IOException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private static Sentence setNerData(CustomsStructuredData customsStructuredData, String nerLabel, boolean retrainPOS, boolean retrainNER) throws IOException, NoSuchFieldException, IllegalAccessException {
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = getPerceptronLexicalAnalyzer(nerLabel, retrainPOS, retrainNER);
		Sentence sentence = perceptronLexicalAnalyzer.analyze(customsStructuredData.getContent());
		List<String> nerResult = new ArrayList<>();
		for (IWord word : sentence.wordList) {
			if (Objects.equals(word.getLabel(), nerLabel)) {
				nerResult.add(word.getValue());
			}
		}
		Field field = customsStructuredData.getClass().getDeclaredField(nerLabel);
		field.setAccessible(true);
		field.set(customsStructuredData, nerResult);
		return sentence;
	}

	/**
	 * 同时处理其他附加字段
	 * @param customsStructuredData 识别结果
	 * @param nerLabel 命名实体
	 * @param retrainPOS 是否需要重新训练POS
	 * @param retrainNER 是否需要重新训练NER
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	private static void setNerDataWithOtherField(CustomsStructuredData customsStructuredData, String nerLabel, boolean retrainPOS, boolean retrainNER) throws IllegalAccessException, NoSuchFieldException, IOException {
		Sentence sentence = setNerData(customsStructuredData, nerLabel, retrainPOS, retrainNER);
		Iterator<IWord> wordIterator = sentence.wordList.iterator();

		// 简陋的数词、量词合并
		// 简陋的时间提取
		String mqKey = "";
		String mqValue = "";
		String time = "";
		List<Pair<String, String>> mqList = new ArrayList<>();
		List<String> timeList = new ArrayList<>();
		while (wordIterator.hasNext()) {
			IWord currentWord = wordIterator.next();
			if (Objects.equals(currentWord.getLabel(), "m")) {
				// 数词
				mqKey = currentWord.getValue();
			} else if (Objects.equals(currentWord.getLabel(), "q") && StringUtils.isNotBlank(mqKey)) {
				// 量词
				mqValue = currentWord.getValue();
				mqList.add(new Pair<>(mqKey, mqValue));
			} else {
				mqKey = "";
			}

			if (Objects.equals(currentWord.getLabel(), "t")) {
				time += currentWord.getValue();
			} else {
				if (StringUtils.isNotBlank(time)) {
					timeList.add(time);
					time = "";
				}
			}
		}
		Field mqField = customsStructuredData.getClass().getDeclaredField(CustomsStructuredDataConstant.NumeralQuantity);
		mqField.setAccessible(true);
		mqField.set(customsStructuredData, mqList);

		Field timeField = customsStructuredData.getClass().getDeclaredField(CustomsStructuredDataConstant.SeizedTime);
		timeField.setAccessible(true);
		timeField.set(customsStructuredData, timeList);
	}
}
