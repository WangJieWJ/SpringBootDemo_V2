package com.machineLearning.encog;

import com.machineLearning.MachineLearningAppTest;
import com.machineLearning.encog.service.EncogClassificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/31 13:58
 */
public class EncogTest extends MachineLearningAppTest {

    @Autowired
    private EncogClassificationService encogClassificationService;

    @Test
    public void encogTest(){
        encogClassificationService.irisTest();
    }

}
