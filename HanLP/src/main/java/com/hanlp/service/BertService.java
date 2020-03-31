package com.hanlp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/30 16:32
 */
public class BertService {

	public static void createBertCorpus(String filePath, String resultFilePath) throws IOException {
		List<String> pku98DataList = FileUtils.readLines(new File(filePath), "UTF-8");
		String[] pku98Array = null;
		StringBuilder builder = new StringBuilder();
		for (String pku98Data : pku98DataList) {
			Sentence sentence = Sentence.create(pku98Data);
			for (IWord word : sentence.wordList) {
				appendData(builder, word.getValue(), word.getLabel());
			}
			builder.append("\n");
		}
		FileOutputStream fileOutputStream = new FileOutputStream(resultFilePath);
		IOUtils.write(builder.toString(), fileOutputStream, "UTF-8");
		fileOutputStream.flush();
	}

	public static void appendData(StringBuilder builder, String word, String pos) {
		int wordLength = word.length();
		String prePos = "";
		String sufPos = "";
		if (Objects.equals(pos, "nt")) {
			// 组织
			prePos = "B-ORG";
			sufPos = "I-ORG";
		} else if (Objects.equals(pos, "t")) {
			// 时间
			prePos = "B-TIM";
			sufPos = "I-TIM";
		} else if (Objects.equals(pos, "ns")) {
			// 地名
			prePos = "B-LOC";
			sufPos = "I-LOC";
		} else if (Objects.equals(pos, "nr")) {
			// 人名
			prePos = "B-PER";
			sufPos = "I-PER";
		} else {
			prePos = "O";
			sufPos = "O";
		}
		for (int i = 0; i < wordLength; i++) {
			builder.append(String.format("%s %s%n", word.charAt(i), i == 0 ? prePos : sufPos));
		}
	}

	public static void main(String[] args) throws IOException {
		String filePath = "/Users/wangjie/Development/ELK/hanlp/语料库/pku98/199801-test.txt";
		String resultFilePath = "/Users/wangjie/Development/nlp/bert/BERT-BiLSTM-CRF-NER-master/data_dir/train.txt";
		createBertCorpus(filePath, resultFilePath);

		String filePath1 = "/Users/wangjie/Development/ELK/hanlp/语料库/pku98/199801-test.txt";
		String resultFilePath1 = "/Users/wangjie/Development/nlp/bert/BERT-BiLSTM-CRF-NER-master/data_dir/test.txt";
		createBertCorpus(filePath1, resultFilePath1);

		String filePath2 = "/Users/wangjie/Development/ELK/hanlp/语料库/pku98/199801-test.txt";
		String resultFilePath2 = "/Users/wangjie/Development/nlp/bert/BERT-BiLSTM-CRF-NER-master/data_dir/dev.txt";
		createBertCorpus(filePath2, resultFilePath2);
	}
}
