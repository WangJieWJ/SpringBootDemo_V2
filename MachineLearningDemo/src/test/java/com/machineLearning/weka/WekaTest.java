package com.machineLearning.weka;

import com.machineLearning.MachineLearningAppTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.converters.DatabaseLoader;
import weka.core.converters.DatabaseSaver;
import weka.experiment.InstanceQuery;

/**
 * Title:
 * Description: Weka基本操作
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/29 14:52
 */
public class WekaTest extends MachineLearningAppTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WekaTest.class);

    /**
     * 从文件中读取数据
     */
    @Test
    public void loadFileData() throws Exception {
        Instances data = ConverterUtils.DataSource.read("/Users/wangjie/Development/BigData/DataMining/Weka/WekaData/Arff/iris.arff");
        System.out.println(data.relationName());
    }

    /**
     * 通过InstanceQuery读取数据库中的数据
     */
    @Test
    public void loadDBDataByInstanceQuery() throws Exception {
        InstanceQuery query = new InstanceQuery();
        query.setDatabaseURL("jdbc:mysql://localhost:3306/local_test_db?useSSL=false");
        query.setUsername("root");
        query.setPassword("123456");
        query.setQuery("select * from iris");
        Instances data = query.retrieveInstances();
    }


    /**
     * 通过DatabaseLoader读取数据库中的数据
     */
    @Test
    public void loadDBDataByDatabaseLoader() throws Exception {
        DatabaseLoader loader = new DatabaseLoader();
        loader.setSource("jdbc:mysql://localhost:3306/local_test_db?useSSL=false", "root",
                "123456");
        loader.setQuery("select * from iris");
        Instances data = loader.getDataSet();
    }


    /**
     * 通过DatabaseLoader增量读取数据库中的数据
     */
    @Test
    public void loadDBDataByDatabaseLoaderIncremental() throws Exception {
        DatabaseLoader loader = new DatabaseLoader();
        loader.setSource("jdbc:mysql://localhost:3306/local_test_db?useSSL=false", "root",
                "123456");
        loader.setQuery("select * from iris");
        Instances structure = loader.getStructure();
        Instances data = new Instances(structure);
        Instance inst;
        while ((inst = loader.getNextInstance(structure)) != null) {
            LOGGER.info("增量加载数据！");
            data.add(inst);
        }
    }


    /**
     * 保存数据到文件中
     */
    @Test
    public void writeDataToFile() throws Exception {
        Instances data = ConverterUtils.DataSource.read("/Users/wangjie/Development/BigData/DataMining/Weka/WekaData/Arff/iris.arff");
        ConverterUtils.DataSink.write("/Users/wangjie/Development/BigData/DataMining/Weka/WekaData/Arff/iris_write.csv", data);
    }

    /**
     * 报错数据到数据库中
     */
    @Test
    public void writeDataToDB() throws Exception {
        Instances data = ConverterUtils.DataSource.read("/Users/wangjie/Development/BigData/DataMining/Weka/WekaData/Arff/iris.arff");
        DatabaseSaver saver = new DatabaseSaver();
        saver.setDestination("jdbc:mysql://localhost:3306/local_test_db?useSSL=false", "root", "123456");
        saver.setTableName("write_iris");
        saver.setRelationForTableName(false);
        saver.setInstances(data);
        saver.writeBatch();
    }


}
