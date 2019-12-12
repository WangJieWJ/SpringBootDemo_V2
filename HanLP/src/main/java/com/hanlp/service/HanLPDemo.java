package com.hanlp.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.excel.EasyExcel;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.corpus.FileDataSet;
import com.hankcs.hanlp.classification.corpus.IDataSet;
import com.hankcs.hanlp.classification.corpus.MemoryDataSet;
import com.hankcs.hanlp.classification.statistics.evaluations.Evaluator;
import com.hankcs.hanlp.classification.statistics.evaluations.FMeasure;
import com.hankcs.hanlp.classification.tokenizers.BigramTokenizer;
import com.hankcs.hanlp.classification.tokenizers.HanLPTokenizer;
import com.hankcs.hanlp.classification.tokenizers.ITokenizer;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hanlp.classifiers.LinearSVMClassifier;
import com.hanlp.dto.AutoCatData;
import com.hanlp.listener.AutoCatDataListener;
import com.trs.ckm.soap.CATModelInfo;
import com.trs.ckm.soap.CATRevDetail;
import com.trs.ckm.soap.CkmSoapException;
import com.trs.ckm.soap.Constants;
import com.trs.ckm.soap.TrsCkmSoapClient;

import org.springframework.stereotype.Service;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-14 22:46
 */
@Service
public class HanLPDemo {

	/**
	 * 词典加载测试
	 * @param dictionaryPath 词典路径
	 * @throws IOException
	 */
	public static void loadDictionaryDemo(String dictionaryPath) throws IOException {
		TreeMap<String, CoreDictionary.Attribute> dictionary = IOUtil.loadDictionary(dictionaryPath);
		System.out.println(String.format("词典大小：%s个词条", dictionary.size()));
		System.out.println(String.format("输出词典的第一个词条为：%s", dictionary.keySet().iterator().next()));
	}

	/**
	 * HanLP默认分词
	 * @param text 待分词
	 */
	private static void segment(String text) {
		List<Term> termList = HanLP.segment(text);
		termList.forEach(term -> System.out.println(String.format("word:%s,offset:%s", term.word, term.offset)));
	}

	/**
	 * 从内容中获取几个关键词
	 * @param context    内容
	 * @param keyWordNum 关键词数量
	 */
	private static void ectractKeyWordDemo(String context, int keyWordNum) {
		List<String> keyWordList = HanLP.extractKeyword(context, keyWordNum);
		for (String keyWord : keyWordList) {
			System.out.println(keyWord);
		}
	}

	/**
	 * HANLP 两种分词器、两种分类器，相互组合
	 * @throws IOException
	 */
	private static void textClassificationDemo() throws IOException {
		String folderPath = "/Users/wangjie/Development/ELK/hanlp/语料库/搜狗文本分类语料库迷你版";
		textClassificationDemo1(folderPath, new NaiveBayesClassifier(), new HanLPTokenizer());
		textClassificationDemo1(folderPath, new NaiveBayesClassifier(), new BigramTokenizer());
		textClassificationDemo1(folderPath, new LinearSVMClassifier(), new HanLPTokenizer());
		textClassificationDemo1(folderPath, new LinearSVMClassifier(), new BigramTokenizer());
	}

	/**
	 * HANLP 文本分类
	 * @param folderPath 待分类的文件路径
	 * @param classifier 分类器
	 * @param tokenizer 分词器
	 */
	private static void textClassificationDemo1(String folderPath, IClassifier classifier, ITokenizer tokenizer) throws IOException {
		// 前90%作为训练集
		IDataSet trainingCorpus = new FileDataSet()
				.setTokenizer(tokenizer)
				.load(folderPath, "UTF-8", 0.9);
		classifier.train(trainingCorpus);
		// 后10%作为测试集
		IDataSet testingCorpus = new MemoryDataSet(classifier.getModel()).
				load(folderPath, "UTF-8", -0.1);
		// 计算准确率
		FMeasure result = Evaluator.evaluate(classifier, testingCorpus);
		System.out.println(classifier.getClass().getSimpleName() + "+" + tokenizer.getClass().getSimpleName());
		System.out.println(result);
	}

	/**
	 * HANLP 情感分析
	 */
	private static void emotionAnalysisDemo() throws IOException {

		String folderPathComment = "/Users/wangjie/Development/ELK/hanlp/语料库/ChnSentiCorp情感分析酒店评论";
		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
		naiveBayesClassifier.train(folderPathComment);

		LinearSVMClassifier linearSVMClassifier = new LinearSVMClassifier();
		linearSVMClassifier.train(folderPathComment);

		String text1 = "热水器不热";
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text1, naiveBayesClassifier.classify(text1)));
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text1, linearSVMClassifier.classify(text1)));

		String text2 = "结果大失所望，灯光昏暗，空间极其狭小，床垫质量恶劣，房间还伴着一股霉味。";
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text2, naiveBayesClassifier.classify(text2)));
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text2, linearSVMClassifier.classify(text2)));

		String text3 = "可利用文本分类实现情感分析，效果还行";
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text3, naiveBayesClassifier.classify(text3)));
		System.out.println(String.format("当前需要分类的模板：%s，模板类型为：%s", text3, linearSVMClassifier.classify(text3)));

	}


	/**
	 * CKM 自动分类
	 */
	private static void createAutoCatFile() {
		String authCatFileName = "/Users/wangjie/Development/项目/海关/CKM/自动分类训练.xlsx";
		EasyExcel.read(authCatFileName, AutoCatData.class, new AutoCatDataListener()).sheet().doRead();
	}

	/**
	 * CKM 自动分类测试
	 * @throws CkmSoapException
	 */
	public static void ckmDemo() throws CkmSoapException {
		TrsCkmSoapClient trsCkmSoapClient = new TrsCkmSoapClient("http://127.0.0.1:8000", "admin", "trsadmin");
		String autoCatModelName = "haiGuanModel";
		// 读取自动分类模板
		CATModelInfo catModelInfo = trsCkmSoapClient.CATGetModelInfo(autoCatModelName,
				Constants.MODEL_TYPE_AUTO_CURRENT);
		if (catModelInfo != null) {
			System.out.println(catModelInfo.getcreatedate());
			System.out.println(catModelInfo.getxmlcatinfo());
			System.out.println(catModelInfo.gettype());
		}
		String detailContent1 = "10月29日，海关总署风险防控局（青岛）布控查获禁止进境固体废物71.4吨\t\n"
				+ "根据该局布控指令，查获经上海口岸申报原产自西班牙的次级铝塑板实为禁止进境固体废物情事，目前已做退运处理。";

		String detailContent2 = "10月15日南宁海关依法退运禁止进口固体废物1329.7吨\t\n"
				+ "为上海某公司今年1-3月从北海口岸申报进口的“高碳铬铁”，经鉴定样品中疏松多孔状部分为冶炼过程中残留的炉渣，属我国禁止进口固体废物，该关依法责令退运出境。";

		String detailContent3 = "杭州海关缉私局经对前期侦办案件的深挖扩线，于1月2日立案侦办一起走私成品油案，抓获犯罪嫌疑人1名，"
				+ "冻结涉案账户资金100余万元。经查，2018年9月至12月，犯罪嫌疑人陈某雇佣、组织人员先后20余次驾驶油船前往东经122度、北纬27度海域过驳柴油走私入境至温州销售牟利，"
				+ "查证涉嫌走私柴油1万余吨，案值6000余万元，涉嫌偷逃税款2000余万元。";

		String detailContent4 = "1月7日上海海关对某公司进口非抗癌药物实施补税1296.5万元。根据海关总署税收征管局（京津）指令，"
				+ "对某制药公司2018年5-9月申报进口的醋酸奥曲肽注射液（善宁）实施验估作业。经验核财务数据、合同、发票等相关资料，"
				+ "该商品实为非抗癌药，不应享受抗癌药物税收优惠政策，涉及货值9973.5万元。";

		String detailContent5 = "\"2018年6月15日，我科对深圳市恒基拓展实业有限公司委托中国外运华南有限公司黄埔分公司持报关单520120181018027415以一般贸易方式向我关申报的铅矿砂一批进行查验。"
				+ "查验关员现场发现实际到货为灰色泥块状物，与一般铅矿砂原矿呈相对均质颗粒状、有色泽的特征不符。经与风险部门共同研判，需要做固废鉴定检测。"
				+ "6月29日，取样送广州海关化验中心核品名、成份、归类及是否固废。7月13日接广州海关化验中心出具的GZ2018072374号中国海关进出境货物（物品）化验鉴定证书，"
				+ "结论为：送检样品主要成份为硫酸铅，送检样品不是正常矿物，请核实来源后确定归类。11月9日，中国检验认证集团广东有限公司派工作人员到现场取样。"
				+ "12月25日，接中国检验认证集团广东有限公司出具的441118110014号检验证书和防城港出入境检验检疫局综合技术服务中心出具的2018FCGWT0009号鉴别报告，结论为：送检样品不是铅矿砂，来源于废铅酸蓄电池渣泥，属于目前我国禁止进口的固体废物。"
				+ "经查并根据上述证书，该单申报进口铅矿砂未到货。另有废铅酸蓄电池渣泥131302千克未申报。2019年1月2日移交处置处理。目前此案正在进一步处理中。企业涉嫌通过伪报品名方式进口国家禁限进口的固体废物，逃避海关监管。"
				+ "建议对上述查发企业及相关货物进行布控，必要时取样送检或送固废鉴定。";

		Map<String, String> param = new HashMap<>();
		param.put("生态类_1", detailContent1);
		param.put("生态类_2", detailContent2);
		param.put("经济类_3", detailContent3);
		param.put("经济类_4", detailContent4);
		param.put("生态类_5", detailContent5);


		for (Map.Entry<String, String> itemMap : param.entrySet()) {
			CATRevDetail[] catRevDetails = trsCkmSoapClient.CATClassifyText(autoCatModelName, itemMap.getValue());
			System.out.println(String.format("正确分类:%s", itemMap.getKey()));
			if (catRevDetails != null) {
				for (CATRevDetail catRevDetail : catRevDetails) {
					System.out.println(String.format("\t 预测：%s,置信度：%s", catRevDetail.getCATName(), catRevDetail.getv()));
				}
			}
		}
	}


	public static void main(String[] args) throws Exception {
//		loadDictionaryDemo("/Users/wangjie/Development/ELK/hanlp/data/dictionary/CoreNatureDictionary.txt");
//		textClassificationDemo();
//		emotionAnalysisDemo();
		createAutoCatFile();
//		segment("东莞海关查获一批进口医疗器械外包装标签违反“一个中国”原则的情事。2019年10月16日，东莞海关查验部门在对一批从中国台湾进口的61套、价值375546元人民币的医疗器械进行查验时，发现该批医疗器械的外包装标签上标注了“ROC”字样，违反了“一个中国”的原则。该关按照规定责令企业进行整改，并清除相关标签。");

	}
}
