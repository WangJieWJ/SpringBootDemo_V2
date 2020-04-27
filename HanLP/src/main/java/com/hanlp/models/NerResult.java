package com.hanlp.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/26 11:06
 */
public class NerResult implements Iterable<NerInfo> {

	private List<NerInfo> nerInfos = new ArrayList<>();

	public void addNerInfo(NerInfo nerInfo) {
		nerInfos.add(nerInfo);
	}

	public List<NerInfo> getNerInfos() {
		return nerInfos;
	}

	@Override
	public Iterator<NerInfo> iterator() {
		return nerInfos.iterator();
	}

	@Override
	public String toString() {
		return "NerResult{" +
				"nerInfos=" + nerInfos +
				'}';
	}
}