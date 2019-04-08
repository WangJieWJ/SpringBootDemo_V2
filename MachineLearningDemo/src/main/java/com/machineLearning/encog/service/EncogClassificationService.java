package com.machineLearning.encog.service;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Title:
 * Description: Encog分类测试
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/31 14:43
 */
@Service
public class EncogClassificationService {

    private static final String ENCOG_DATA_PATH = "/Users/wangjie/Development/BigData/DataMining/Encog/data";

    private static final Logger LOGGER = LoggerFactory.getLogger(EncogClassificationService.class);

    public void irisTest() {
        //1、Mapping the Input File
        VersatileDataSource source = new CSVDataSource(new File(ENCOG_DATA_PATH + "/iris.csv"),
                false, CSVFormat.DECIMAL_POINT);
        VersatileMLDataSet data = new VersatileMLDataSet(source);
        data.defineSourceColumn("sepallength", 0, ColumnType.continuous);
        data.defineSourceColumn("sepalwidth", 1, ColumnType.continuous);
        data.defineSourceColumn("petallength", 2, ColumnType.continuous);
        data.defineSourceColumn("petalwidth", 3, ColumnType.continuous);
        ColumnDefinition outputColumn = data.defineSourceColumn("species", 4, ColumnType.nominal);
        data.analyze();

        //2、Specifying the Model & Normalizing
        data.defineSingleOutputOthersInput(outputColumn);
        EncogModel model = new EncogModel(data);
        model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);
        model.setReport(new ConsoleStatusReportable());
        data.normalize();

        //3、Fitting the Model
        model.holdBackValidation(0.3, true, 1001);
        model.selectTrainingType(data);
        MLRegression bestMethod = (MLRegression) model.crossvalidate(5, true);

        //4、Displaying the Results
        LOGGER.info("Training Error:{}", EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));

        LOGGER.info("Validation Error:{}", EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

        NormalizationHelper helper = data.getNormHelper();
        LOGGER.info(helper.toString());

        LOGGER.info(bestMethod.toString());

        //5、Using the Model & Denormalizing
        ReadCSV csv = new ReadCSV(new File(ENCOG_DATA_PATH + "/iris_train_data.csv"), false, CSVFormat.EG_FORMAT);
        String[] line = new String[4];
        MLData input = helper.allocateInputVector();
        while (csv.next()) {
            line[0] = csv.get(0);
            line[1] = csv.get(1);
            line[2] = csv.get(2);
            line[3] = csv.get(3);
            helper.normalizeInputVector(line, input.getData(), false);
            MLData output = bestMethod.compute(input);
            String predicted = helper.denormalizeOutputVectorToString(output)[0];
            LOGGER.info("预测结果:{},实际结果:{},是否正确:{}", predicted, csv.get(4), predicted.equals(csv.get(4)));
        }
        Encog.getInstance().shutdown();
    }

}
