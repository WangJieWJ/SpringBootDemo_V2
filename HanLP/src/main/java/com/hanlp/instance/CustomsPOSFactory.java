package com.hanlp.instance;

import java.io.IOException;

import com.hankcs.hanlp.model.perceptron.POSTrainer;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/9 16:51
 */
public class CustomsPOSFactory {

	public static LinearModel getPOSLinearModel(String trainingFile, String modelPath) throws IOException {
		POSTrainer posTrainer = new POSTrainer();
		return posTrainer.train(trainingFile, null, modelPath, 0, 5, Runtime.getRuntime().availableProcessors()).getModel();
	}
}
