## 常用接口
+ 命名实体识别接口 
    + NERecognizer
+ 词性标注接口  
    + POSTagger
+ 分词器接口
    + Segmenter
    
## 常用接口的简单封装
+ LexicalAnalyzer 继承自上面三个接口，分别实现以下类
    + AbstractLexicalAnalyzer
        + CRF词法分析器(CRFLexicalAnalyzer)
        + 基于隐马尔可夫模型的词法分析器(HMMLexicalAnalyzer)
        + 感知机词法分析器，支持简繁全半角和大小写(PerceptronLexicalAnalyzer)
    + 流水线式词法分析器(LexicalAnalyzerPipeline)

## 分词算法
+ Segment
    + DictionaryBasedSegment[基于词典的机械分词器基类]
        + AhoCorasickDoubleArrayTrieSegment[使用AhoCorasickDoubleArrayTrie实现的最长分词器]
        + DoubleArrayTrieSegment[使用DoubleArrayTrie实现的最长分词器]
    + CharacterBasedSegment[基于“由字构词”方法分词器基类]
        + AbstractLexicalAnalyzer
            + CRFLexicalAnalyzer[CRF词法分析器（中文分词、词性标注和命名实体识别）]
            + HMMLexicalAnalyzer[基于隐马尔可夫模型的词法分析器]
            + PerceptronLexicalAnalyzer[感知机词法分析器，支持简繁全半角和大小写]
                + 支持在线学习
        + HMMSegment[基于2阶HMM（A Second-Order Hidden Markov Model, TriGram3阶文法模型）+ BMES序列标注的分词器]
    + WordBasedSegment[基于词语NGram模型的分词器基类]
        + DijkstraSegment[最短路径分词]
        + NShortSegment[N最短分词器]
        + ViterbiSegment[Viterbi分词器<br>]
    + SegmentPipeline[流线性分词操作]
    
## 常用场景
+ 人名识别
    + 人名词典(/Users/wangjie/Development/ELK/hanlp/data/dictionary/person/)(非.txt后缀的文件属于快速加载文件)
        + 中国人名识别
            + nr.txt[中国人名词典]
            + PersonDictionary[对应实体类]
            + NR[人名角色标签——人名标注集]
        + 日本人名识别
            + nrj.txt[日本人名词典]
            + JapanesePersonDictionary[对应实体类]
        + 音译人名识别
            + nrf.txt[音译人名词典]
            + TranslatedPersonDictionary[对应实体类]
    + 人名识别类
        + PersonRecognition[中国人名识别]
        + JapanesePersonRecognition[日本人名识别]
        + TranslatedPersonRecognition[音译人名识别]

+ 地名识别
    + 地名词典(/Users/wangjie/Development/ELK/hanlp/data/dictionary/place)
        + ns.txt[地名词典]
        + PlaceDictionary[对应实体类]
        + NS[地名角色标签——地名标注集]
    + 地名识别类
        + PlaceRecognition[地址识别]
        
+ 机构识别
    + 机构词典(/Users/wangjie/Development/ELK/hanlp/data/dictionary/organization)
        + nt.txt[机构词典]
        + OrganizationDictionary[对应实体类]
        + NT[机构角色标签——机构标注集]
    + 机构识别类
        + OrganizationRecognition[机构识别]
    
+ 拼音转换
    + 拼音词典(/Users/wangjie/Development/ELK/hanlp/data/dictionary/pinyin)
        + pinyin.txt[拼音词典]
        + PinyinDictionary[对应实体类]

+ 停用词
    + 停用词词典(data/dictionary)
        + stopwords.txt[停用词词典]
        + CoreStopWordDictionary[对应实体类]——支持热更新

+ 翻译
    + HongKongToSimplifiedChineseDictionary[香港繁体转简体]
    + HongKongToTaiwanChineseDictionary[香港繁体转台湾繁体]
    + HongKongToTraditionalChineseDictionary[香港繁体转繁体]
    + SimplifiedChineseDictionary[简体转繁体词典]
    + SimplifiedToHongKongChineseDictionary[简体转香港繁体繁体]
    + SimplifiedToTaiwanChineseDictionary[简体转台湾繁体]
    + TaiwanToHongKongChineseDictionary[台湾繁体转香港繁体]
    + TaiwanToSimplifiedChineseDictionary[台湾繁体转简体]
    + TaiwanToTraditionalChineseDictionary[台湾繁体转繁体]
    + TraditionalChineseDictionary[繁体转简体]
    + TraditionalToHongKongChineseDictionary[繁体转香港繁体]
    + TraditionalToTaiwanChineseDictionary[繁体转台湾繁体]

+ 文本聚类
    + ClusterAnalyzer
    
+ 文本分类
    + NaiveBayesClassifier[实现一个基于多项式贝叶斯模型的文本分类器]
    + LinearSVMClassifier[基于SVM支持向量机的文本分类器]
  
+ 利用互信息和左右熵的短语提取器
    + MutualInformationEntropyPhraseExtractor

+ 文本推荐
    + Suggester文本推荐器
        + BaseScorer[打分器]
            + EditDistanceScorer[编辑距离打分器]
            + IdVectorScorer[单词语义向量打分器]
            + PinyinScorer[拼音打分器]

+ 自动生成摘要
    + TextRankSentence[TextRank 自动摘要]
    + TextRankKeyword[基于TextRank算法的关键字提取，适用于单文档]