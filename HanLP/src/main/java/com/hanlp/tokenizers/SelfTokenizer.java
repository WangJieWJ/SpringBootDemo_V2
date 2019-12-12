package com.hanlp.tokenizers;

import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.tokenizers.ITokenizer;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

/**
 * Title: 
 * Description:  自定义分词器，支持指定分词算法
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-12 16:07
 */
public class SelfTokenizer implements ITokenizer {


	/**
	 * 默认分词器
	 */
	private Segment segment = HanLP.newSegment();


	public SelfTokenizer() {
	}

	public SelfTokenizer(Segment segment) {
		this.segment = segment;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	@Override
	public String[] segment(String text) {
		List<Term> termList = segment.seg(text);
		termList.removeIf(term -> !CoreStopWordDictionary.shouldInclude(term));
		termList.removeIf(term -> term.word.indexOf('\u0000') >= 0);
		String[] termArray = new String[termList.size()];
		int p = -1;
		for (Term term : termList) {
			termArray[++p] = term.word;
		}
		return termArray;
	}

}
