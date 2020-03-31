package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.SegmentPipeline;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 基于HanLP的命名实体识别
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-18 10:00
 */
public class HanLPRecognition {

	private static final Logger LOGGER = LoggerFactory.getLogger(HanLPRecognition.class);

	/**
	 * Viterbi分词器
	 */
	public static void viterbiSegmentDemo(String segmentText) {
		ViterbiSegment viterbiSegment = new ViterbiSegment();
		viterbiSegment.enableAllNamedEntityRecognize(true);
//		viterbiSegment.enableNumberQuantifierRecognize(true);
		viterbiSegment.enablePartOfSpeechTagging(true);
		System.out.println("开始进行viterbiSegment");
		viterbiSegment.seg(segmentText).forEach(term -> {
			System.out.print(String.format("%s ", term));
		});
	}

	/**
	 * N最短分词器
	 */
	public static void nShortSegmentDemo(String segmentText) {
		System.out.println("\nN最短分词器");
		NShortSegment nShortSegment = new NShortSegment();
		nShortSegment.enableAllNamedEntityRecognize(true);
		nShortSegment.enableNumberQuantifierRecognize(true);
		nShortSegment.seg(segmentText).forEach(term -> {
			System.out.print(String.format("%s ", term));
		});
	}

	/**
	 * 最短路径分词
	 */
	public static void dijkstraSegmentDemo(String segmentText) {
		System.out.println("\n最短路径分词");
		DijkstraSegment dijkstraSegment = new DijkstraSegment();
		dijkstraSegment.enableAllNamedEntityRecognize(true);
		dijkstraSegment.enableNumberQuantifierRecognize(true);
		dijkstraSegment.seg(segmentText).forEach(term -> {
			System.out.print(String.format("%s ", term));
		});
	}

	/**
	 * CRF词法分析器
	 */
	public static Sentence CRFLexicalAnalyzerDemo(String segmentText) throws IOException {
		CRFLexicalAnalyzer crfLexicalAnalyzer = new CRFLexicalAnalyzer();
		crfLexicalAnalyzer.enableNumberQuantifierRecognize(true);
		return crfLexicalAnalyzer.analyze(segmentText);
	}

	/**
	 * 感知器分词器
	 */
	public static Sentence PerceptronLexicalAnalyzerDemo(String segmentText) throws IOException {
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = new PerceptronLexicalAnalyzer();
		return perceptronLexicalAnalyzer.analyze(segmentText);
	}

	public static void selfDisplayRealmModelBasedOnPerceptron(String segmentText) {
		String PLANE_CORPUS = "/Users/wangjie/Development/ELK/hanlp/data/测试程序使用到的数据/plane-re/train.txt";
		String PLANE_MODEL = "/Users/wangjie/Development/ELK/hanlp/data/测试程序使用到的数据/plane-re/model.bin";
	}


	public static void main(String[] args) throws IOException {
		List<String> originDataList = FileUtils.readLines(new File("/Users/wangjie/Development/项目/海关/二期结构化处理/待结构化文本.txt"), "UTF-8");
////		HanLP.Config.enableDebug(true);
		List<String> perceptronResultList = new ArrayList<>();
		List<String> crfResultList = new ArrayList<>();
		for (String originData : originDataList) {
////			System.out.println("------------------------------------------------------------------------------------------------");
////			viterbiSegmentDemo(originData);
////			nShortSegmentDemo(originData);
////			dijkstraSegmentDemo(originData);
//			System.out.println(CRFLexicalAnalyzerDemo(originData));
//			System.out.println(PerceptronLexicalAnalyzerDemo(originData));
			perceptronResultList.add(PerceptronLexicalAnalyzerDemo(originData).toString());
			crfResultList.add(CRFLexicalAnalyzerDemo(originData).toString());
////			break;
		}
		SegmentPipeline pipeline = new SegmentPipeline(new PerceptronLexicalAnalyzer());
//		pipeline.add()
		FileUtils.writeLines(new File("/Users/wangjie/Development/项目/海关/二期结构化处理/perceptron.txt"), perceptronResultList, "\n");
		FileUtils.writeLines(new File("/Users/wangjie/Development/项目/海关/二期结构化处理/crf.txt"), crfResultList, "\n");
	}
}
