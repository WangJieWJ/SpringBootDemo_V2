package com.hanlp.models;

import com.hankcs.hanlp.classification.features.IFeatureWeighter;
import com.hankcs.hanlp.classification.models.AbstractModel;
import de.bwaldvogel.liblinear.Model;

/**
 * Title: 
 * Description: 线性SVM模型
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-12 13:45
 */
public class LinearSVMModel extends AbstractModel {

	/**
	 * 训练样本数
	 */
	public int n = 0;

	/**
	 * 类别数
	 */
	public int c = 0;

	/**
	 * 特征数
	 */
	public int d = 0;

	/**
	 * 特征权重计算工具
	 */
	public IFeatureWeighter featureWeighter;

	/**
	 * SVM分类模型
	 */
	public Model svmModel;
}
