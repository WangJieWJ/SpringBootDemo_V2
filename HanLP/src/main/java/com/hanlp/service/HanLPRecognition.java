package com.hanlp.service;

import java.util.List;

import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
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

	public static void viterbiSegmentDemo(String segmentText) {
		ViterbiSegment viterbiSegment = new ViterbiSegment();
		viterbiSegment.enableAllNamedEntityRecognize(true);
		List<Term> termList = viterbiSegment.seg(segmentText);
		LOGGER.info("开始进行viterbiSegment");
		long countNum = termList.stream().peek(term -> {
			LOGGER.info("\t分词结果:{}，词性:{}", term.word, term.nature);
		}).count();
		LOGGER.info("结束进行viterbiSegment，分词总数为:{}", countNum);
	}

	public static void main(String[] args) {
		String segmentText = "日前，在金陵海关驻新生圩办事处监管下，此前由该办查获的424.158吨限制进境固体废物被原样退运出境。该批货物于今年9月进境，申报品名为“净瓶片”或“再生颗粒”，涉及18个集装箱，重量424.158吨，货值187.61万元。海关关员现场查验时发现异常，经取样送权威部门化验鉴定，确认该批货物实际为“废塑料”，为国家限制进境固体废物。目前该7票废塑料均已办理责令退运。";
		viterbiSegmentDemo(segmentText);
	}
}
