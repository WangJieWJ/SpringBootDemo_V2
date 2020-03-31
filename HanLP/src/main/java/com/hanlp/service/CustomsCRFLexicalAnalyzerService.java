package com.hanlp.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.crf.CRFNERecognizer;
import com.hankcs.hanlp.model.crf.CRFPOSTagger;
import com.hankcs.hanlp.model.crf.CRFSegmenter;
import com.hankcs.hanlp.model.crf.CRFTagger;
import com.hankcs.hanlp.model.crf.crfpp.Encoder;
import com.hankcs.hanlp.model.crf.crfpp.crf_learn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/2/25 21:36
 */
public class CustomsCRFLexicalAnalyzerService {

	private static CRFLexicalAnalyzer crfLexicalAnalyzer;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomsCRFLexicalAnalyzerService.class);

	public static void createCRFCorpus() throws IOException {
		String trainFile = "";

		File tmpTemplate = File.createTempFile("crfpp-template-" + new Date().getTime(), ".txt");
		tmpTemplate.deleteOnExit();
		String templFile = tmpTemplate.getAbsolutePath();
		String template = "templateData";
		IOUtil.saveTxt(templFile, template);

		File tmpTrain = File.createTempFile("crfpp-train-" + new Date().getTime(), ".txt");
		tmpTrain.deleteOnExit();
		CRFTagger crfTagger = new CRFNERecognizer(null);
		crfTagger.convertCorpus(trainFile, tmpTrain.getAbsolutePath());
		trainFile = tmpTrain.getAbsolutePath();
//		System.out.printf("Java效率低，建议安装CRF++，执行下列等价训练命令（不要终止本进程，否则临时语料库和特征模板将被清除）：\n" +
//						"crf_learn -m %d -f %d -e %f -c %f -p %d -H %d -a %s -t %s %s %s\n", maxitr, freq, eta,
//				C, threadNum, shrinkingSize, algorithm.toString().replace('_', '-'),
//				templFile, trainFile, modelFile);
	}

	public static void trainCRFAnalyzer(){

	}
	static {
		try {
			String trainingFile = "/Users/wangjie/Development/项目/海关/二期结构化处理/crf_SeizedOrganization.txt";
			// 1、分词器
			CRFSegmenter crfSegmenter = new CRFSegmenter();

			// 2、词性标注器"model.bin", "pos.bin"
			CRFPOSTagger crfposTagger = new CRFPOSTagger(null); // 创建空白标注器
			String posModelPath = "/Users/wangjie/Development/项目/海关/二期结构化处理/crf_SeizedOrganization_pos.txt";
			crfposTagger.train(trainingFile, posModelPath); // 训练
			crfposTagger = new CRFPOSTagger(posModelPath); // 加载

			// 3、命名实体的识别
			CRFNERecognizer crfneRecognizer = new CRFNERecognizer(null);
			crfneRecognizer.getNERTagSet().nerLabels.clear();
			crfneRecognizer.addNERLabels("SeizedOrganization");
			crf_learn.Option option = new crf_learn.Option();
			// 参照词性标注设置特征数量
			String nerModelPath = "/Users/wangjie/Development/项目/海关/二期结构化处理/crf_SeizedOrganization_ner.txt";
			crfneRecognizer.train(trainingFile, nerModelPath, option.maxiter, 10, option.eta, option.cost,
					option.thread, option.shrinking_size, Encoder.Algorithm.fromString(option.algorithm));
			crfneRecognizer = new CRFNERecognizer(nerModelPath);

			crfLexicalAnalyzer = new CRFLexicalAnalyzer(crfSegmenter, crfposTagger, crfneRecognizer);
		} catch (IOException e) {
			LOGGER.error("构架结构化CRF词法分析器失败！", e);
		}
	}

	public static void main(String[] args) throws IOException {
		List<String> crfList = Arrays.asList(
				"汕头海关隶属潮汕机场海关查获国家管制类药品“氟硝西泮”94片。9月22日，根据汕头海关隶属潮汕机场海关邮递物品监管科移交线索，采取控制下交付，在普宁市流沙北街道附近抓获犯罪嫌疑人秦某，现场缴获从日本进境的邮包1个，内装国家管制类药品“氟硝西泮”94片。目前已对该案刑事立案侦办，已对嫌疑人采取刑事拘留强制措施，案件正进一步依法侦办中。",
				"马村港海关查获进口煤炭品质不合格情事。2019年8月4日中国海口外轮代理有限公司申报一批进口菲律宾动力煤，提单重量为54850.680吨，收货人为绥芬河国林木业诚投资有限公司，使用企业为海口马村华能电厂。经抽样检验鉴定，该批次菲律宾动力煤的全水分与合同约定值不相符。",
				"10月16日，黄岛海关查发一起伪报品名骗取出口退税案。经对2票申报为洗净制刷用山羊毛的货物现场查验，发现仅有山羊毛1箱，其他货物全部为化纤制假发，申报货值169.6万美元，涉嫌骗取退税约119万元人民币。",
				"金普海关提醒关注进口危险化学品SDS不合格的风险。近日，金普海关出口加工区B区监管科检验过程中连续发现3批次进口危险化学品不合格情况：报关单号：090420191040042698、报关单号：090420191040043026、报关单号：090420191040043052，不合格原因为现场未随附或丢失中文SDS（Safety Data Sheet，安全数据单）。检验中发现，危险货物SDS不合格的原因主要有：1，SDS未随附货物的物流过程中，而是通过邮件或其他方式传递。2，SDS不规范、数据与分类不匹配。3，SDS资料丢失。"
		);
		crfList.forEach(crfItem -> {
			System.out.println(crfLexicalAnalyzer.analyze(crfItem));
		});

	}
}
