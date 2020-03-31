package com.hanlp.service;

import java.io.IOException;
import java.util.List;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.model.perceptron.NERTrainer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronNERecognizer;
import com.hankcs.hanlp.model.perceptron.PerceptronPOSTagger;
import com.hankcs.hanlp.model.perceptron.PerceptronSegmenter;
import com.hankcs.hanlp.model.perceptron.feature.FeatureMap;
import com.hankcs.hanlp.model.perceptron.instance.Instance;
import com.hankcs.hanlp.model.perceptron.instance.NERInstance;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;
import com.hankcs.hanlp.model.perceptron.utility.Utility;
import com.hanlp.instance.CustomsNERInstanceForSeizedOrganization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: PerceptronLexicalAnalyzer 支持在线学习
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/2/25 16:04
 */
public class CustomsPerceptronLexicalAnalyzerService {

	private static PerceptronLexicalAnalyzer perceptronLexicalAnalyzer;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomsPerceptronLexicalAnalyzerService.class);

	static {

	}

	public static void trainPerceptronLexicalAnalyzerModel() {
		try {
			String trainingFile = "/Users/wangjie/Development/项目/海关/二期结构化处理/perceptron_SeizedOrganization.txt";
			String modelPath = "/Users/wangjie/Development/项目/海关/二期结构化处理/perceptron_SeizedOrganization_ner.bin";

			// 1、训练一个分词器(因为语料库就是根据默认感知器分词器生成的，所以此处直接默认的分词语料)
			// CWSTrainer cwsTrainer = new CWSTrainer();
			// String cwsTrainingFile = "/Users/wangjie/Development/ELK/hanlp/语料库/pku98/199801.txt";
			// LinearModel cwsModel = cwsTrainer.train(cwsTrainingFile, null, modelPath.replace("ner.bin", "cws.bin"), 0, 100, Runtime.getRuntime().availableProcessors()).getModel();
			String cwsModel = "/Users/wangjie/Development/ELK/hanlp/data/model/perceptron/large/cws.bin";
			PerceptronSegmenter perceptronSegmenter = new PerceptronSegmenter(cwsModel);

			// 2、训练一个词性标注
			// POSTrainer posTrainer = new POSTrainer();
			// String posTrainingFile = "/Users/wangjie/Development/ELK/hanlp/语料库/pku98/199801.txt";
			// LinearModel posModel = posTrainer.train(posTrainingFile, null, modelPath.replace("ner.bin", "pos.bin"), 0, 100, Runtime.getRuntime().availableProcessors()).getModel();
			String posModel = "/Users/wangjie/Development/ELK/hanlp/data/model/perceptron/pku1998/pos.bin";
			PerceptronPOSTagger perceptronPOSTagger = new PerceptronPOSTagger(posModel);

			// 3、训练命名实体的
			NERTrainer nerTrainer = new NERTrainer() {
				@Override
				protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
					NERTagSet tagSet = (NERTagSet) featureMap.tagSet;
					List<String[]> collector = Utility.convertSentenceToNER(sentence, tagSet);
					String[] wordArray = new String[collector.size()];
					String[] posArray = new String[collector.size()];
					String[] tagArray = new String[collector.size()];
					Utility.reshapeNER(collector, wordArray, posArray, tagArray);
					return new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, tagArray, tagSet, featureMap);
				}
			};
			nerTrainer.tagSet.nerLabels.clear();
//			nerTrainer.tagSet.nerLabels.add("SeizedTime");
			nerTrainer.tagSet.nerLabels.add("SeizedOrganization");
//			nerTrainer.tagSet.nerLabels.add("SeizedLocation");
//			nerTrainer.tagSet.nerLabels.add("InvolveCompany");
//			nerTrainer.tagSet.nerLabels.add("InvolveUser");
			LinearModel nerModel = nerTrainer.train(trainingFile, null, modelPath, 0, 200, Runtime.getRuntime().availableProcessors()).getModel();
			PerceptronNERecognizer recognizer = new PerceptronNERecognizer(nerModel) {

				@Override
				public boolean learn(String segmentedTaggedNERSentence) {
					return super.learn(createInstance(Sentence.create(segmentedTaggedNERSentence), model.featureMap));
				}

				@Override
				protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
					NERTagSet tagSet = (NERTagSet) featureMap.tagSet;
					List<String[]> collector = Utility.convertSentenceToNER(sentence, tagSet);
					String[] wordArray = new String[collector.size()];
					String[] posArray = new String[collector.size()];
					String[] tagArray = new String[collector.size()];
					Utility.reshapeNER(collector, wordArray, posArray, tagArray);
					return new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, tagArray, tagSet, featureMap);
				}

				@Override
				public String[] recognize(String[] wordArray, String[] posArray) {
					NERInstance instance = new CustomsNERInstanceForSeizedOrganization(wordArray, posArray, model.featureMap);
					return super.recognize(instance);
				}
			};

			// 4、进行结构化感知器标注框架构建
			perceptronLexicalAnalyzer = new PerceptronLexicalAnalyzer(perceptronSegmenter, perceptronPOSTagger, recognizer);
//			analyzer.enableTranslatedNameRecognize(false) // 是否启用音译人名识别
//					.enableJapaneseNameRecognize(false)   // 关闭日本人名的识别
//					.enableAllNamedEntityRecognize(true);          // 启用用户自定义词典
		} catch (IOException e) {
			LOGGER.error("结构化感知器标注框架训练失败！", e);
		}
	}

	public static void useBestPerceptronLexicalAnalyzerModel() {

	}

	public static void main(String[] args) throws IOException {
		trainPerceptronLexicalAnalyzerModel();
		String[] corpusData = new String[] {
				"12月24日，杭州海关风控分局查获瞒报烟花一批。义乌海关根据预定式布控指令对一票申报品名为“蜡烛”等的货物查验，查获该批货物包生日烟花共349件、610800支。",
				"5月16日，香港海关在旺角区采取反冒牌物品执法行动，捣破区内五个小贩摊档，共检获7110件疑似冒牌物品，包括首饰、钱包和手袋，估计市值约40万元。",
				"5月14日，印度海关在港口出口处进行例行检查时，发现9名来自伊斯兰卡科伦坡的乘客形迹可疑，海关人员在对他们进行X光检查之后，发现这9名乘客在下体内共计藏匿了2.93kg的24K纯度的黄金。",
				"东莞海关查获一批进口医疗器械外包装标签违反“一个中国”原则的情事。2019年10月16日，东莞海关查验部门在对一批从中国台湾进口的61套、价值375546元人民币的医疗器械进行查验时，发现该批医疗器械的外包装标签上标注了“ROC”字样，违反了“一个中国”的原则。该关按照规定责令企业进行整改，并清除相关标签",
				"10月15日南宁海关依法退运禁止进口固体废物1329.7吨。为上海某公司今年1-3月从北海口岸申报进口的“高碳铬铁”，经鉴定样品中疏松多孔状部分为冶炼过程中残留的炉渣，属我国禁止进口固体废物，该关依法责令退运出境。",
				"汕头海关信息处查获检疫性害虫“红火蚁”。9月10日，汕头海关隶属汕头港海关再度查获检疫性害虫“红火蚁”，该关对来自香港的30个标箱空箱体抽查时，在其中2个标箱体中截获有害生物活体红火蚁蚁窝和蚂路，立即彻查该批空箱体，并依照相关法律法规对该批空箱体实施严格熏蒸除害处理，有效防止外来有害生物入境，特提请加强空箱检疫，高度重视空箱体源头管理，杜绝携带有害生物入境，确保国门安全",
				"北京海关缉私部门接报后立即出警，于当日对犯罪嫌疑人立案侦查。据犯罪嫌疑人陈某冰供述，其于1997年移居巴西，后取得巴西永久居民身份。在巴西期间与一名男性华裔贩毒团伙成员结识，受其指使将两个装有毒品的行李箱带至中国深圳后交与“接货人”。从巴西出发前，贩毒团伙成员与其交接了装有奶粉罐的行李箱，并为其购买了从巴西到中国的往返机票，承诺事成之后支付3000巴西里亚尔作为报酬",
				"2019年10月22日，呼和浩特海关所属乌拉特海关监管二科在进境卡口物流监控验环节，对车号为3922的蒙古籍运煤车辆进行机检检查，发现其车厢前部煤层下方位置疑似夹藏。经人工检查查获夹藏于该车辆车厢前部的中药材防风682.5千克。目前该情事已移交缉私部门做进一步处理。",
				"商品和服务"
		};
		for (String data : corpusData) {
			System.out.println(perceptronLexicalAnalyzer.analyze(data));
		}
	}

}
