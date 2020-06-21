package com.hanlp.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
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
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.perceptron.parser.KBeamArcEagerDependencyParser;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.pipe.LexicalAnalyzerPipeline;
import com.hankcs.hanlp.tokenizer.pipe.Pipe;
import com.hankcs.hanlp.tokenizer.pipe.RegexRecognizePipe;
import com.hanlp.classifiers.LinearSVMClassifier;
import com.hanlp.dto.AutoCatData;
import com.hanlp.listener.AutoCatDataListener;
import com.hanlp.tokenizers.SelfTokenizer;
import com.trs.ckm.soap.CATModelInfo;
import com.trs.ckm.soap.CATRevDetail;
import com.trs.ckm.soap.CkmSoapException;
import com.trs.ckm.soap.Constants;
import com.trs.ckm.soap.RevDetail;
import com.trs.ckm.soap.RevHold;
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
//		folderPath = "/Users/wangjie/Development/iso/share/ckm/autoCat";
		String charsetName = "UTF-8";
//		textClassificationDemo1(folderPath, charsetName, new NaiveBayesClassifier(), new BigramTokenizer());
//		textClassificationDemo1(folderPath, charsetName, new NaiveBayesClassifier(), new HanLPTokenizer());
		textClassificationDemo1(folderPath, charsetName, new LinearSVMClassifier(), new BigramTokenizer());
		textClassificationDemo1(folderPath, charsetName, new LinearSVMClassifier(), new HanLPTokenizer());
		// 使用自定义的Tokenizer
		textClassificationDemo1(folderPath, charsetName, new LinearSVMClassifier(), new SelfTokenizer());
	}

	/**
	 * HANLP 文本分类
	 * @param folderPath 待分类的文件路径
	 * @param charsetName 编码类型
	 * @param classifier 分类器
	 * @param tokenizer 分词器
	 */
	private static void textClassificationDemo1(String folderPath, String charsetName, IClassifier classifier, ITokenizer tokenizer) throws IOException {
		// 前90%作为训练集
		IDataSet trainingCorpus = new FileDataSet()
				.setTokenizer(tokenizer)
				.load(folderPath, charsetName, 0.9);
		classifier.train(trainingCorpus);
		// 后10%作为测试集
		IDataSet testingCorpus = new MemoryDataSet(classifier.getModel()).
				load(folderPath, charsetName, -0.1);
		// 计算准确率
		FMeasure result = Evaluator.evaluate(classifier, testingCorpus);
		System.out.println(classifier.getClass().getSimpleName() + "+" + tokenizer.getClass().getSimpleName());
		System.out.println(result);
	}

	/**
	 * 使用CRF条件随机场进行分词
	 * @throws IOException
	 */
	public static void testSelfTokenizer() throws IOException {
		// 使用自定的Tokenizer
		String folderPath = "/Users/wangjie/Development/ELK/hanlp/语料库/搜狗文本分类语料库迷你版";
		String charsetName = "UTF-8";
		SelfTokenizer selfTokenizer = new SelfTokenizer();
		Segment selfSegment = new CRFLexicalAnalyzer();
		selfSegment.enableAllNamedEntityRecognize(true);
		// 配置具体的分词算法
		selfTokenizer.setSegment(selfSegment);
		IDataSet trainingCorpus = new FileDataSet()
				.setTokenizer(selfTokenizer)
				.load(folderPath, charsetName, 1);
		LinearSVMClassifier linearSVMClassifier = new LinearSVMClassifier();
		linearSVMClassifier.train(trainingCorpus);

//		System.out.println(JSON.toJSONString(linearSVMClassifier.predict("虽说冯小刚的奔驰车性能特别好,但他从不超速开车。“按规定速度行车,遇到突然的情况,就能够及时处理。否则,就没把握了。“开车上路不要随意超车，尽量少变更车道。超速、超车其实快不了哪儿去，就是你玩儿命超，也就是早个十分钟。前几天在五环路上，唰的一声，一辆车从我车旁飞过，他是从望京的出口出来，在路上正赶上红灯。我跟在他前后脚停住了，但他冒的风险很大。这就是年轻气盛，安全意识不强。要知道，生命是爹妈给的，而生命是非常脆弱的，要保护好。对自己负责，也要对亲人负责。要想想，你还有那么多事要做，还有那么多好日子要过，只是为了把车开快一点，而什么都没有了，太不值了吧！”说起上路感受，冯小刚滔滔不绝。　　几乎每年都要推出一部贺岁片的冯小刚，前几年每年也要“推出”一部汽车。")));
//		System.out.println(JSON.toJSONString(linearSVMClassifier.predict("先是拉达吉普，后又换了一辆夏利、富康、大宇、三凌轿车。现在，他开的是一辆红色奔驰轿车，原因是听别人说红颜色的车发生事故的机率特别低。")));
//		System.out.println(JSON.toJSONString(linearSVMClassifier.predict("汽车之家为汽车消费者提供选车、买车、用车、换车等所有环节的全面、准确、快捷的一站式服务。汽车之家致力于通过产品服务、数据技术、生态规则和资源为用户和")));
//		System.out.println(JSON.toJSONString(linearSVMClassifier.predict("我认为，在开车时与前车保持必要的距离也是至关重要的。我曾经吃过一次亏。那天，在北京双安桥上，我紧跟着前车顺行，忽然前车来了一个急刹车，我来不及停就撞在他后面了。当时车速是30迈，可我觉得震动是很大的。试想，如果是高速行车追尾，那后果一定不堪设想。有人想从中间加进来，我也绝不跟他们斗气，避免事故才是最重要的。”　　")));
		System.out.println(JSON.toJSONString(linearSVMClassifier.predict("12月20日—26日各海关连续查获大量毒品")));
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
	 * 依存句法分析
	 */
	private static void dependencyParser() throws IOException, ClassNotFoundException {
		IDependencyParser parser = new KBeamArcEagerDependencyParser();
		CoNLLSentence tree = parser.parse("电池非常棒，机身不长，长的是待机，但是屏幕分辨率不高。");
		System.out.println(tree);
		tree = parser.parse("2019年10月22日，呼和浩特海关所属乌拉特海关监管二科在进境卡口物流监控验环节，对车号为3922的蒙古籍运煤车辆进行机检检查，发现其车厢前部煤层下方位置疑似夹藏。经人工检查查获夹藏于该车辆车厢前部的中药材防风682.5千克。目前该情事已移交缉私部门做进一步处理");
		System.out.println(tree);
	}

	private static final Pattern WEB_URL = Pattern.compile("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:(((([a-zA-Z0-9][a-zA-Z0-9\\-]*)*[a-zA-Z0-9]\\.)+((aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(biz|b[abdefghijmnorstvwyz])|(cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(edu|e[cegrstu])|f[ijkmor]|(gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(info|int|i[delmnoqrst])|(jobs|j[emop])|k[eghimnprwyz]|l[abcikrstuvy]|(mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])|(name|net|n[acefgilopruz])|(org|om)|(pro|p[aefghklmnrstwy])|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|(tel|travel|t[cdfghjklmnoprtvwz])|u[agksyz]|v[aceginu]|w[fs]|(δοκιμή|испытание|рф|срб|טעסט|آزمایشی|إختبار|الاردن|الجزائر|السعودية|المغرب|امارات|بھارت|تونس|سورية|فلسطين|قطر|مصر|परीक्षा|भारत|ভারত|ਭਾਰਤ|ભારત|இந்தியா|இலங்கை|சிங்கப்பூர்|பரிட்சை|భారత్|ලංකා|ไทย|テスト|中国|中國|台湾|台灣|新加坡|测试|測試|香港|테스트|한국|xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-3e0b707e|xn\\-\\-45brj9c|xn\\-\\-80akhbyknj4f|xn\\-\\-90a3ac|xn\\-\\-9t4b11yi5a|xn\\-\\-clchc0ea0b2g2a9gcd|xn\\-\\-deba0ad|xn\\-\\-fiqs8s|xn\\-\\-fiqz9s|xn\\-\\-fpcrj9c3d|xn\\-\\-fzc2c9e2c|xn\\-\\-g6w251d|xn\\-\\-gecrj9c|xn\\-\\-h2brj9c|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-j6w193g|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-kprw13d|xn\\-\\-kpry57d|xn\\-\\-lgbbat1ad8j|xn\\-\\-mgbaam7a8h|xn\\-\\-mgbayh7gpa|xn\\-\\-mgbbh1a71e|xn\\-\\-mgbc0a9azcg|xn\\-\\-mgberp4a5d4ar|xn\\-\\-o3cw4h|xn\\-\\-ogbpf8fl|xn\\-\\-p1ai|xn\\-\\-pgbs0dh|xn\\-\\-s9brj9c|xn\\-\\-wgbh1c|xn\\-\\-wgbl6a|xn\\-\\-xkc2al3hye2a|xn\\-\\-xkc2dl3a5ee0h|xn\\-\\-yfro4i67o|xn\\-\\-ygbi2ammx|xn\\-\\-zckzah|xxx)|y[et]|z[amw]))|((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?");

	private static final Pattern EMAIL = Pattern.compile("(\\w+(?:[-+.]\\w+)*)@(\\w+(?:[-.]\\w+)*\\.\\w+(?:[-.]\\w+)*)");

	/**
	 * 分词之前对内容进行正则匹配避免 邮箱、url地址这种特殊字符被拆分
	 */
	private static void regexBeforeSegment() throws IOException {
		LexicalAnalyzerPipeline analyzer = new LexicalAnalyzerPipeline(new PerceptronLexicalAnalyzer());
		// 管道顺序=优先级，自行调整管道顺序以控制优先级
		analyzer.addFirst(new RegexRecognizePipe(WEB_URL, "【网址】"));
		analyzer.addFirst(new RegexRecognizePipe(EMAIL, "【邮件】"));
		// 自己写个管道也并非难事
		analyzer.addLast(new Pipe<List<IWord>, List<IWord>>() {
			@Override
			public List<IWord> flow(List<IWord> input) {
				for (IWord word : input) {
					if ("nx".equals(word.getLabel())) {
						word.setLabel("字母");
					}
				}
				return input;
			}
		});
		String text = "HanLP的项目地址是https://github.com/hankcs/HanLP，联系邮箱abc@def.com";
		System.out.println(analyzer.analyze(text));
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
		String autoCatModelName = "demo";
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

	/**
	 * CKM相似性检测
	 */
	public static void ckmSimModel() throws CkmSoapException {
		TrsCkmSoapClient trsCkmSoapClient = new TrsCkmSoapClient("http://127.0.0.1:8000", "admin", "trsadmin");

		String simModelName = "contentSimModel";

//		 创建相似性模板
//		createCkmSimModel(simModelName, trsCkmSoapClient);

		// 添加相似记录
		updateSimUpdateIndex("1", "空调//@海尔空调:#海尔空调牵手中国女排#今天是海尔空调的大日子，本空调终于和@中国女排 “锁”了！这么激动的事儿得让大家和我一起分享，送空调必须安排上！！转+评本微博，说说你理解的“女排精神”，12.13抽一个宝宝送我家新品【海尔多风感系列】挂机一台！#与风同行，再创新高# 抽奖详情", simModelName, trsCkmSoapClient);
		updateSimUpdateIndex("2", "锦鲤手抄报 空调//@海尔空调:#海尔空调牵手中国女排#今天是海尔空调的大日子，本空调终于和@中国女排 “锁”了！这么激动的事儿得让大家和我一起分享，送空调必须安排上！！转+评本微博，说说你理解的“女排精神”，12.13抽一个宝宝送我家新品【海尔多风感系列】挂机一台！#与风同行，再创新高# 抽奖详情", simModelName, trsCkmSoapClient);
		updateSimUpdateIndex("3", "超级无敌普_通人 空调//@海尔空调:#海尔空调牵手中国女排#今天是海尔空调的大日子，本空调终于和@中国女排 “锁”了！这么激动的事儿得让大家和我一起分享，送空调必须安排上！！转+评本微博，说说你理解的“女排精神”，12.13抽一个宝宝送我家新品【海尔多风感系列】挂机一台！#与风同行，再创新高# 抽奖详情", simModelName, trsCkmSoapClient);
		updateSimUpdateIndex("4", "转发微博 //@海尔智家:#智家小科普#冷柜结霜存在各种隐患，清霜工作却又堪比战场。。。交给我们吧～2019.12.1-2020.1.24，海尔智家35周年感恩节推出回馈老用户活动，免费上门为你提供家电保养；更有感恩新品礼，购新家电享各种惊喜好礼～想趁#双十二#换新的，赶紧看过来！", simModelName, trsCkmSoapClient);

		// 根据文章内容查询相似度
		querySimRetrieveByText("空调//@海尔空调:#海尔空调牵手中国女排#今天是海尔空调的大日子，本空调终于和@中国女排 “锁”了！这么激动的事儿得让大家和我一起分享，送空调必须安排上！！转+评本微博，说说你理解的“女排精神”，12.13抽一个宝宝送我家新品【海尔多风感系列】挂机一台！#与风同行，再创新高# 抽奖详情", simModelName, trsCkmSoapClient);
		querySimRetrieveByText("海尔空调牵手中国女排#说说你理解的“女排精神”，12.13抽一个宝宝送我家新品【海尔多风感系列】挂机一台！#与风同行，再创新高# 抽奖详情", simModelName, trsCkmSoapClient);
	}

	/**
	 * 创建相似性模板
	 * @param simModelName 相似性模板名称
	 * @param trsCkmSoapClient ckm连接
	 * @throws CkmSoapException
	 */
	private static void createCkmSimModel(String simModelName, TrsCkmSoapClient trsCkmSoapClient) throws CkmSoapException {
		System.out.println(String.format("相似性模板创建结果:%s", trsCkmSoapClient.SimCreateModel(simModelName)));
	}

	/**
	 * 添加相似记录模板
	 * @param contentId 内容ID
	 * @param contentDetail 内容
	 * @param simModelName 模板名称
	 * @param trsCkmSoapClient ckm连接
	 * @throws CkmSoapException
	 */
	private static void updateSimUpdateIndex(String contentId, String contentDetail, String simModelName, TrsCkmSoapClient trsCkmSoapClient) throws CkmSoapException {
		System.out.println(String.format("添加相似性记录结果:%s", trsCkmSoapClient.SimUpdateIndex(simModelName, contentId, contentDetail)));
	}

	/**
	 * 根据文章内容查询相似度
	 * @param contentDetail 内容
	 * @param simModelName 模型名称
	 * @param trsCkmSoapClient ckm连接
	 * @throws CkmSoapException
	 */
	private static void querySimRetrieveByText(String contentDetail, String simModelName, TrsCkmSoapClient trsCkmSoapClient) throws CkmSoapException {
		RevHold revHold = new RevHold();
		RevDetail[] revDetails = trsCkmSoapClient.SimRetrieveByText(simModelName, revHold, contentDetail);
		if (revDetails != null) {
			for (RevDetail revDetail : revDetails) {
				System.out.println(String.format("相似文章Id:%s,相似度:%s", revDetail.getid(), revDetail.getsimv()));
			}
		}
	}

	public static void main(String[] args) throws Exception {
//		loadDictionaryDemo("/Users/wangjie/Development/ELK/hanlp/data/dictionary/CoreNatureDictionary.txt");
//		textClassificationDemo();
//		testSelfTokenizer();
//		dependencyParser();
//		ckmDemo();
//		regexBeforeSegment();
//		emotionAnalysisDemo();
//		createAutoCatFile();
//		ckmSimModel();
		PerceptronLexicalAnalyzer perceptronLexicalAnalyzer = new PerceptronLexicalAnalyzer();
		System.out.println(perceptronLexicalAnalyzer.analyze("玻璃纤维网格布为玻璃纤维机织物经进一步涂胶处理的产品，从商品的基本特征看，仍属于玻璃纤维机织物的商品范畴，根据归类总规则一及六，经参考w-2-5100-2010-0102归类指导意见，“玻璃纤维网格布”应归入税则号列7019.5900"));
		System.out.println(perceptronLexicalAnalyzer.analyze("青岛桑迪益科工贸有限公司于2019年5月14日以一般贸易方式申报出口太阳能热水器配件(集热管)等，报关单号425820190000436990。经查验并归类认定，第1项货物应归入税号7020009990（出口退税率为零，无出口监管证件）"));;

//		segment("东莞海关查获一批进口医疗器械外包装标签违反“一个中国”原则的情事。2019年10月16日，东莞海关查验部门在对一批从中国台湾进口的61套、价值375546元人民币的医疗器械进行查验时，发现该批医疗器械的外包装标签上标注了“ROC”字样，违反了“一个中国”的原则。该关按照规定责令企业进行整改，并清除相关标签。");
	}
}
