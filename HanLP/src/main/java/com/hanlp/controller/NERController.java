package com.hanlp.controller;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hanlp.models.CustomsPerceptronLexicalAnalyzerFactory;
import com.hanlp.models.CustomsStructuredData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/10 11:30
 */
@RestController
@RequestMapping("/ner")
@Api(value = "nerDemo", description = "命名实体识别Demo")
public class NERController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NERController.class);

	@PostMapping(value = "analyzer")
	@ApiOperation(value = "文本数据结构化")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "待格式化的文本", name = "content", paramType = "query", required = true, dataType = "String")
	})
	public CustomsStructuredData analyzer(String content) {
		CustomsStructuredData customsStructuredData = null;
		try {
			customsStructuredData = CustomsPerceptronLexicalAnalyzerFactory.getCustomsStructuredData(content);
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			LOGGER.error("文本标注失败！", e);
		}
		return customsStructuredData;
	}

	@PostMapping(value = "learn")
	@ApiOperation(value = "模型学习")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "原文", name = "originContent", paramType = "query", required = true, dataType = "String"),
			@ApiImplicitParam(value = "已经标注好的文本", name = "content", paramType = "query", required = true, dataType = "String"),
			@ApiImplicitParam(value = "学习次数", name = "learnNum", paramType = "query", required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(value = "命名实体", name = "nerLabel", paramType = "query", required = true, dataType = "String")
	})
	public CustomsStructuredData learn(String originContent, String content, Integer learnNum, String nerLabel) {
		CustomsStructuredData customsStructuredData = null;
		try {
			PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = CustomsPerceptronLexicalAnalyzerFactory.getPerceptronLexicalAnalyzer(nerLabel, false, false);
			for (int i = 0; i < learnNum; i++) {
				perceptronLexicalAnalyzer.learn(content);
			}
			customsStructuredData = CustomsPerceptronLexicalAnalyzerFactory.getCustomsStructuredData(originContent);
		} catch (IOException | IllegalAccessException | NoSuchFieldException e) {
			LOGGER.error("模型学习失败！", e);
		}
		return customsStructuredData;
	}

	@PostMapping(value = "save")
	@ApiOperation(value = "模型保存")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "要保存的命名实体", name = "nerLabel", paramType = "query", required = true, dataTypeClass = String.class),
			@ApiImplicitParam(value = "词性标注模型是否需要保存", name = "POSModelSave", paramType = "query", required = true, dataTypeClass = Boolean.class),
			@ApiImplicitParam(value = "命名实体识别模型是否需要保存", name = "NERModelSave", paramType = "query", required = true, dataTypeClass = Boolean.class)
	})
	public JSONObject save(String nerLabel, boolean POSModelSave, boolean NERModelSave) {
		JSONObject modelSaveResult = new JSONObject();
		try {
			modelSaveResult = CustomsPerceptronLexicalAnalyzerFactory.savePerceptronLexicalAnalyzerModel(nerLabel, POSModelSave, NERModelSave);
		} catch (IOException e) {
			LOGGER.error("模型保存失败！", e);
			modelSaveResult.put("msg", "模型保存失败！");
		}
		return modelSaveResult;
	}

}
