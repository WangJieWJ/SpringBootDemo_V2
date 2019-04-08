package com.machineLearning.weka;

import com.machineLearning.MachineLearningApp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Title:
 * Description: Weka常用算法测试
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/29 17:27
 */
public class WekaAlgorithm extends MachineLearningApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(WekaAlgorithm.class);

    private static final String WEKA_DATA_PATH = "/Users/wangjie/Development/BigData/DataMining/Weka/WekaData/Arff/";

    /**
     * 测试线性回归
     */
    @Test
    public void testLinearRegression() throws Exception {
        Instances data = ConverterUtils.DataSource.read(WEKA_DATA_PATH + "house.arff");
        // 最后一列存放数据的结果
        data.setClassIndex(data.numAttributes() - 1);
        LinearRegression linearRegression = new LinearRegression();
        linearRegression.buildClassifier(data);
        double[] codf = linearRegression.coefficients();
        LOGGER.info("\n系数1:{}\n系数2:{}\n系数3:{}\n系数4:{}\n系数5:{}\n系数6:{}\n常量:{}",
                codf[0], codf[1], codf[2], codf[3], codf[4], codf[5], codf[6]);

        double trainingValue = (codf[0] * 3529) +
                (codf[1] * 9191) +
                (codf[2] * 6) +
                (codf[3] * 0) +
                (codf[4] * 0) +
                codf[6];

        LOGGER.info("根据得到的线性模型，计算结果的:{},实际结果:{}", trainingValue, 205000);

        double predictionValue = (codf[0] * 3198) +
                (codf[1] * 9669) +
                (codf[2] * 5) +
                (codf[3] * 3) +
                (codf[4] * 1) +
                codf[6];
        LOGGER.info("根据得到的线性模型，预测结果为:{}", predictionValue);
    }

    @Test
    public void testRandomForest() throws Exception {
        Instances data = ConverterUtils.DataSource.read(WEKA_DATA_PATH + "segment-challenge.arff");
        data.setClassIndex(data.numAttributes() - 1);
        RandomForest rf = new RandomForest();
        // 设置为0表示没有任何限制
        rf.setNumExecutionSlots(100);
        rf.buildClassifier(data);



        String[] options = rf.getOptions();
        for (String option : options) {
            System.out.println(option);
        }
    }

}
