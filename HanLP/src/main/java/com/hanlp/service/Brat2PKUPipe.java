package com.hanlp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.document.sentence.word.CompoundWord;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.tokenizer.lexical.AbstractLexicalAnalyzer;
import com.hankcs.hanlp.tokenizer.pipe.Pipe;
import com.hanlp.constants.CustomsStructuredDataConstant;
import com.hanlp.models.BratAnnInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.CollectionUtils;

/**
 * Title: 
 * Description: 将brat标注转换成PKU
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/2/27 10:23
 */
public class Brat2PKUPipe implements Pipe<List<IWord>, List<IWord>> {

	private String annFilePath;

	private AbstractLexicalAnalyzer abstractLexicalAnalyzer;

	private List<String> nerTag;

	private int startIndex;

	private int endIndex;

	private static final Logger LOGGER = LoggerFactory.getLogger(Brat2PKUPipe.class);

	public Brat2PKUPipe(String annFilePath, AbstractLexicalAnalyzer abstractLexicalAnalyzer, List<String> nerTag, int startIndex, int endIndex) {
		this.annFilePath = annFilePath;
		this.abstractLexicalAnalyzer = abstractLexicalAnalyzer;
		this.nerTag = nerTag;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public List<IWord> flow(List<IWord> input) {
		ListIterator<IWord> listIterator = input.listIterator();
		while (listIterator.hasNext()) {
			IWord wordOrSentence = listIterator.next();
			if (Objects.nonNull(wordOrSentence.getLabel())) {
				continue; // 这是别的管道已经处理过的单词，跳过
			}
			listIterator.remove(); // 否则是句子
			String sentence = wordOrSentence.getValue();

			try {
				int begin = 0;
				int end;
				List<String> annStrList = FileUtils.readLines(new File(annFilePath), "UTF-8");
				List<BratAnnInfo> bratAnnInfoList = new ArrayList<>();
				for (String annStr : annStrList) {
					String[] annArray = annStr.split("\t", 3);
					String[] nerArray = annArray[1].split(" ", 3);
					if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.StartPort)) {
						nerArray[0] = CustomsStructuredDataConstant.StartCountry;
					} else if (Objects.equals(nerArray[0], CustomsStructuredDataConstant.EndPort)) {
						nerArray[0] = CustomsStructuredDataConstant.EndCountry;
					}
					if (!nerTag.contains(nerArray[0])) {
						continue;
					}
					if (Integer.parseInt(nerArray[1]) < startIndex || Integer.parseInt(nerArray[2]) > endIndex) {
						continue;
					}

					BratAnnInfo bratAnnInfo = new BratAnnInfo(nerArray[0], annArray[2],
							Integer.parseInt(nerArray[1]) - startIndex,
							Integer.parseInt(nerArray[2]) - startIndex);

					bratAnnInfoList.add(bratAnnInfo);
				}

				if (CollectionUtils.isEmpty(bratAnnInfoList)) {
//					System.out.println(String.format("当前的句子未进行标注:%s", wordOrSentence));
					return null;
				}

				// 按照startIndex进行排序
				bratAnnInfoList.sort(BratAnnInfo::compareTo);

				for (BratAnnInfo bratAnnInfo : bratAnnInfoList) {
					end = bratAnnInfo.getStartIndex();
					if (end > begin) {
						// 存在未拦截到的部分
						List<IWord> beforeWordList = abstractLexicalAnalyzer.analyze(sentence.substring(begin, end)).wordList;
						for (IWord word : beforeWordList) {
							listIterator.add(word);
						}
					}
					List<Word> nerWord = abstractLexicalAnalyzer.analyze(bratAnnInfo.getOriginName()).wordList.stream().map(iWord -> new Word(iWord.getValue(), iWord.getLabel())).collect(Collectors.toList());
					if (nerWord.size() == 1) {
						// 简单词用CWS+POS
//						System.out.printf("简单词为:%s，词性为:%s%n", nerWord.get(0).getValue(), bratAnnInfo.getNerName());
						listIterator.add(new Word(nerWord.get(0).getValue(), bratAnnInfo.getNerName()));
					} else {
						// 复合词用NER
						listIterator.add(new CompoundWord(nerWord, bratAnnInfo.getNerName())); // 拦截到的部分
					}
					begin = bratAnnInfo.getEndIndex();
				}
				if (begin < sentence.length()) {
					List<IWord> afterWordList = abstractLexicalAnalyzer.analyze(sentence.substring(begin)).wordList;
					for (IWord word : afterWordList) {
						listIterator.add(word);
					}
				}
			} catch (IOException e) {
				LOGGER.error("读取Brat标注文件失败！", e);
			}
		}
		return input;
	}

	/**
	 *  生成PKU格式的预料库
	 * @param corpusDataPath /Users/wangjie/Development/nlp/brat-1.3p1/data/customs/StructuredData.txt
	 * @param nerLabel 识别命名实体
	 * 						SeizedTime
	 * 						SeizedLocation
	 * 						InvolveUser
	 * 						InvolveCompany
	 * 						DeclareGoods
	 * 						RealGoods
	 * 						StartPort
	 * 						StartCountry
	 * 						EndCountry
	 * 						EndPort
	 * @param analyzer 分类器
	 */
	public static StringBuilder resolveBratFile(String corpusDataPath, String nerLabel, AbstractLexicalAnalyzer analyzer) throws IOException {

		List<String> corpusDataList = FileUtils.readLines(new File(corpusDataPath), "UTF-8");

		List<String> nerTag = new ArrayList<>();
		nerTag.add(nerLabel);

		StringBuilder wordBuilder = new StringBuilder();
		int startIndex = 0;
		int endIndex = 0;
		for (String corpusData : corpusDataList) {
			endIndex = startIndex + corpusData.length();
			Brat2PKUPipe pkuPipe = new Brat2PKUPipe(corpusDataPath.replace(".txt", ".ann"),
					analyzer, nerTag, startIndex, endIndex);
			List<IWord> task = new LinkedList<>();
			task.add(new Word(corpusData, null));
			List<IWord> wordList = pkuPipe.flow(task);
			if (Objects.nonNull(wordList)) {
				StringJoiner wordJoiner = new StringJoiner(" ");
				for (IWord word : wordList) {
					if (word instanceof Word) {
						word.setValue(word.getValue().replaceAll("[ 　 ]+", ""));
						if (StringUtils.isBlank(word.getValue())) {
							continue;
						}
						word = new Word(word.getValue(), word.getLabel());
					} else if (word instanceof CompoundWord) {
						List<Word> innerList = ((CompoundWord) word).innerList;
						innerList = innerList.stream()
								.map(itemWord -> {
									itemWord.setValue(itemWord.getValue().replaceAll("[ 　 ]+", ""));
									return itemWord;
								}).filter(itemWord -> StringUtils.isNotBlank(itemWord.getValue())).collect(Collectors.toList());
						word = new CompoundWord(innerList, word.getLabel());
					}
					wordJoiner.add(word.toString());
				}
				wordBuilder.append(wordJoiner.toString());
				if (wordJoiner.length() > 0) {
					wordBuilder.append("\n");
				}
			}
			startIndex += (corpusData.length() + 1);
		}

		return wordBuilder;
	}

	/**
	 * 将数据保存为语料数据
	 * @param wordBuilder 待保存的数据
	 * @param nerLabel 待识别的命名实体
	 * @param abstractLexicalAnalyzerClass 生成具体类型的
	 * @throws IOException
	 */
	public static void saveCorpusData(StringBuilder wordBuilder, String nerLabel, Class<? extends AbstractLexicalAnalyzer> abstractLexicalAnalyzerClass) throws IOException {
		String analyzerModel = abstractLexicalAnalyzerClass == PerceptronLexicalAnalyzer.class ? "perceptron" : "crf";
		String resultFilePath = String.format("/Users/wangjie/Development/项目/海关/二期结构化处理/%s_%s.txt", analyzerModel, nerLabel);
		FileOutputStream fileOutputStream = new FileOutputStream(resultFilePath);
		IOUtils.write(wordBuilder.toString(), fileOutputStream, "UTF-8");
		fileOutputStream.flush();
	}

	public static void createCorpusByAnalyzer(AbstractLexicalAnalyzer abstractLexicalAnalyzer) throws IOException {
		List<String> nerList = Arrays.asList(CustomsStructuredDataConstant.SeizedOrganization,
				CustomsStructuredDataConstant.SeizedLocation,
				CustomsStructuredDataConstant.DeclareGoods,
				CustomsStructuredDataConstant.RealGoods,
				CustomsStructuredDataConstant.StartCountry,
				CustomsStructuredDataConstant.EndCountry,
				CustomsStructuredDataConstant.InvolveUser,
				CustomsStructuredDataConstant.InvolveCompany);

		StringBuilder wordBuilder = new StringBuilder();
		for (String nerLabel : nerList) {
			wordBuilder.setLength(0);
//			while (wordBuilder.length() < 200000) {
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/external-2020.txt", nerLabel, abstractLexicalAnalyzer));
			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/internal-2019.txt", nerLabel, abstractLexicalAnalyzer));
			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/nationwide-2020.txt", nerLabel, abstractLexicalAnalyzer));
			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/nationwide-2019.txt", nerLabel, abstractLexicalAnalyzer));
			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/nlp/brat-1.3p1/data/customs/internal-2020.txt", nerLabel, abstractLexicalAnalyzer));
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/internal-2020.txt", nerLabel, abstractLexicalAnalyzer));
//			}
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/internal-2019.txt", nerLabel, abstractLexicalAnalyzer));
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/internal-2019.txt", nerLabel, abstractLexicalAnalyzer));
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/internal-2019.txt", nerLabel, abstractLexicalAnalyzer));
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/external-2020.txt", nerLabel, abstractLexicalAnalyzer));
//			wordBuilder.append(resolveBratFile("/Users/wangjie/Development/项目/海关/二期结构化处理/标注的领域语料库/StructuredData.txt", nerLabel, abstractLexicalAnalyzer));
			saveCorpusData(wordBuilder, nerLabel, abstractLexicalAnalyzer instanceof PerceptronLexicalAnalyzer ? PerceptronLexicalAnalyzer.class : CRFLexicalAnalyzer.class);
		}
	}

	public static void main(String[] args) throws IOException {
		// 生成结构化感知器的语料库
		AbstractLexicalAnalyzer abstractLexicalAnalyzer = null;

		abstractLexicalAnalyzer = new PerceptronLexicalAnalyzer(HanLP.Config.PerceptronCWSModelPath, HanLP.Config.PerceptronPOSModelPath);
		abstractLexicalAnalyzer = new CRFLexicalAnalyzer();

		abstractLexicalAnalyzer.enableAllNamedEntityRecognize(false);
		createCorpusByAnalyzer(abstractLexicalAnalyzer);
		// 生成CRF的语料库
//		createCorpusByAnalyzer(new CRFLexicalAnalyzer());
	}
}
