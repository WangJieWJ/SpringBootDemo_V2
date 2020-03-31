package com.hanlp.models;

import java.io.IOException;

import com.hankcs.hanlp.model.perceptron.NERTrainer;
import com.hankcs.hanlp.model.perceptron.POSTrainer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronNERecognizer;
import com.hankcs.hanlp.model.perceptron.PerceptronPOSTagger;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/2/27 15:43
 */
public class BratAnnInfo implements Comparable<BratAnnInfo> {

	private String nerName;

	private String originName;

	private int startIndex;

	private int endIndex;

	public BratAnnInfo(String nerName, String originName, int startIndex, int endIndex) {
		this.nerName = nerName;
		this.originName = originName;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public int compareTo(BratAnnInfo bratAnnInfo) {
		return bratAnnInfo.startIndex >= this.startIndex ? -1 : 1;
	}

	public String getNerName() {
		return nerName;
	}

	public void setNerName(String nerName) {
		this.nerName = nerName;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public static void main(String[] args) throws IOException {

		String corpusFile = "/Users/wangjie/Development/ELK/hanlp/自定义预料/customCorpus.txt";

		// 1、训练一个分词器(因为语料库就是根据默认感知器分词器生成的，所以此处直接默认的分词语料)
		String cwsModel = "/Users/wangjie/Development/ELK/hanlp/data/model/perceptron/large/cws.bin";
		PerceptronSegmenter perceptronSegmenter = new PerceptronSegmenter(cwsModel);

		// 2、训练一个词性标注
		POSTrainer posTrainer = new POSTrainer();
		String posModelFile = corpusFile.replace(".txt", ".pos.bin");
		LinearModel posModel = posTrainer.train(corpusFile, null, posModelFile).getModel();
		PerceptronPOSTagger perceptronPOSTagger = new PerceptronPOSTagger(posModel);

		// 3、训练命名实体的
		NERTrainer nerTrainer = new NERTrainer();
		nerTrainer.tagSet.nerLabels.clear();
		nerTrainer.tagSet.nerLabels.add("country");
		String nerModelFile = corpusFile.replace(".txt", ".ner.bin");
		LinearModel nerModel = nerTrainer.train(corpusFile, null, nerModelFile).getModel();
		PerceptronNERecognizer recognizer = new PerceptronNERecognizer(nerModel);

		// 4、运用训练好之后结构化感知器进行 命名实体识别
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = new PerceptronLexicalAnalyzer(perceptronSegmenter, perceptronPOSTagger, recognizer);

	}
}
