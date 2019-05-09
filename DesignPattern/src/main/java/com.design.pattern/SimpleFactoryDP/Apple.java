package com.design.pattern.SimpleFactoryDP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 14:21
 */
public class Apple implements Fruit {

    private static final Logger LOGGER = LoggerFactory.getLogger(Apple.class);

    @Override
    public void eat() {
        LOGGER.info("this is apple！");
    }
}
