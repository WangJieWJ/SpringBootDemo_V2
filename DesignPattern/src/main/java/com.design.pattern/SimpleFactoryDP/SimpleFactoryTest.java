package com.design.pattern.SimpleFactoryDP;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 15:13
 */
public class SimpleFactoryTest {

    public static void main(String[] args) {
        Fruit apple = FruitFactory.produceFruit("apple");
        apple.eat();

        Fruit orange = FruitFactory.produceFruit("orange");
        orange.eat();
    }
}
