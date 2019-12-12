package com.hanlp.classifiers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.hankcs.hanlp.classification.classifiers.AbstractClassifier;
import com.hankcs.hanlp.classification.corpus.Document;
import com.hankcs.hanlp.classification.corpus.IDataSet;
import com.hankcs.hanlp.classification.features.BaseFeatureData;
import com.hankcs.hanlp.classification.features.ChiSquareFeatureExtractor;
import com.hankcs.hanlp.classification.features.DfFeatureData;
import com.hankcs.hanlp.classification.features.IFeatureWeighter;
import com.hankcs.hanlp.classification.features.TfIdfFeatureWeighter;
import com.hankcs.hanlp.classification.models.AbstractModel;
import com.hankcs.hanlp.classification.tokenizers.ITokenizer;
import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;
import com.hankcs.hanlp.utility.MathUtility;
import com.hanlp.models.LinearSVMModel;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-12 13:44
 */
public class LinearSVMClassifier extends AbstractClassifier {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinearSVMClassifier.class);

	LinearSVMModel model;

	public LinearSVMClassifier() {
	}

	public LinearSVMClassifier(LinearSVMModel model) {
		this.model = model;
	}

	@Override
	public Map<String, Double> predict(String text) throws IllegalArgumentException, IllegalStateException {
		if (model == null) {
			throw new IllegalStateException("未训练模型！无法执行预测！");
		}
		if (text == null) {
			throw new IllegalArgumentException("参数 text == null");
		}

		//分词，创建文档
		Document document = new Document(model.wordIdTrie, model.tokenizer.segment(text));

		return predict(document);
	}

	@Override
	public double[] categorize(Document document) throws IllegalArgumentException, IllegalStateException {
		FeatureNode[] x = buildDocumentVector(document, model.featureWeighter);
		double[] probs = new double[model.svmModel.getNrClass()];
		Linear.predictProbability(model.svmModel, x, probs);
		return probs;
	}

	@Override
	public void train(IDataSet dataSet) {
		if (dataSet.size() == 0) {
			throw new IllegalArgumentException("训练数据集为空,无法继续训练");
		}
		// 选择特征
		DfFeatureData featureData = selectFeatures(dataSet);
		// 构造权重计算逻辑
		IFeatureWeighter weighter = new TfIdfFeatureWeighter(dataSet.size(), featureData.df);
		// 构造SVM问题
		Problem problem = createLiblinearProblem(dataSet, featureData, weighter);
		// 释放内存
		BinTrie<Integer> wordIdTrie = featureData.wordIdTrie;
		featureData = null;
		ITokenizer tokenizer = dataSet.getTokenizer();
		String[] catalog = dataSet.getCatalog().toArray();
		dataSet = null;
		System.gc();
		// 求解SVM问题
		Model svmModel = solveLibLinearProblem(problem);
		// 将有用的数据留下来
		model = new LinearSVMModel();
		model.tokenizer = tokenizer;
		model.wordIdTrie = wordIdTrie;
		model.catalog = catalog;
		model.svmModel = svmModel;
		model.featureWeighter = weighter;
	}

	@Override
	public AbstractModel getModel() {
		return model;
	}

	private Model solveLibLinearProblem(Problem problem) {
		de.bwaldvogel.liblinear.Parameter lparam = new Parameter(SolverType.L1R_LR,
//                                                                 grid.find_parameters(problem, 500, 505, 1),
				500.,
				0.01);
		return de.bwaldvogel.liblinear.Linear.train(problem, lparam);
	}

	private Problem createLiblinearProblem(IDataSet dataSet, BaseFeatureData baseFeatureData, IFeatureWeighter weighter) {
		Problem problem = new Problem();
		int n = dataSet.size();
		problem.l = n;
		problem.n = baseFeatureData.featureCategoryJointCount.length;
		problem.x = new FeatureNode[n][];
		// 最新版liblinear的y数组是浮点数
		problem.y = new double[n];
		Iterator<Document> iterator = dataSet.iterator();
		for (int i = 0; i < n; i++) {
			// 构造文档向量
			Document document = iterator.next();
			problem.x[i] = buildDocumentVector(document, weighter);
			// 设置样本的y值
			problem.y[i] = document.category;
		}

		return problem;
	}

	private FeatureNode[] buildDocumentVector(Document document, IFeatureWeighter weighter) {
		// 词的个数
		int termCount = document.tfMap.size();
		FeatureNode[] x = new FeatureNode[termCount];
		Iterator<Map.Entry<Integer, int[]>> tfMapIterator = document.tfMap.entrySet().iterator();
		for (int j = 0; j < termCount; j++) {
			Map.Entry<Integer, int[]> tfEntry = tfMapIterator.next();
			int feature = tfEntry.getKey();
			int frequency = tfEntry.getValue()[0];
			// liblinear 要求下标从1开始递增
			x[j] = new FeatureNode(feature + 1,
					weighter.weight(feature, frequency));
		}
		// 对向量进行归一化
		double normalizer = 0;
		for (int j = 0; j < termCount; j++) {
			double weight = x[j].getValue();
			normalizer += weight * weight;
		}
		normalizer = Math.sqrt(normalizer);
		for (int j = 0; j < termCount; j++) {
			double weight = x[j].getValue();
			x[j].setValue(weight / normalizer);
		}

		return x;
	}

	/**
	 * 统计特征并且执行特征选择，返回一个FeatureStats对象，用于计算模型中的概率
	 *
	 * @param dataSet
	 * @return
	 */
	protected DfFeatureData selectFeatures(IDataSet dataSet) {
		ChiSquareFeatureExtractor chiSquareFeatureExtractor = new ChiSquareFeatureExtractor();

		//FeatureStats对象包含文档中所有特征及其统计信息
		//执行统计
		DfFeatureData featureData = new DfFeatureData(dataSet);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("使用卡方检测选择特征中...");
		//我们传入这些统计信息到特征选择算法中，得到特征与其分值
		Map<Integer, Double> selectedFeatures = chiSquareFeatureExtractor.chi_square(featureData);

		//从训练数据中删掉无用的特征并重建特征映射表
		String[] wordIdArray = dataSet.getLexicon().getWordIdArray();
		int[] idMap = new int[wordIdArray.length];
		Arrays.fill(idMap, -1);
		featureData.wordIdTrie = new BinTrie<Integer>();
		featureData.df = new int[selectedFeatures.size()];
		int p = -1;
		for (Integer feature : selectedFeatures.keySet()) {
			++p;
			featureData.wordIdTrie.put(wordIdArray[feature], p);
			featureData.df[p] = MathUtility.sum(featureData.featureCategoryJointCount[feature]);
			idMap[feature] = p;
		}
		stringBuilder.append(String.format(",选中特征数:%d / %d = %.2f%%\n", selectedFeatures.size(),
				featureData.featureCategoryJointCount.length,
				MathUtility.percentage(selectedFeatures.size(), featureData.featureCategoryJointCount.length)));
		stringBuilder.append("缩减训练数据中...");
		int n = dataSet.size();
		dataSet.shrink(idMap);
		stringBuilder.append(String.format("缩减了 %d 个样本,剩余 %d 个样本\n", n - dataSet.size(), dataSet.size()));
		LOGGER.info(stringBuilder.toString());

		return featureData;
	}
}
