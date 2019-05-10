package com.design.pattern.ChainOfResponsibilityDP;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description: 责任链设计模式
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 15:52
 */
public class ChainOfResponsibilityTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(ChainOfResponsibilityTest.class);

    /**
     * 一个纯的责任链模式要求一个具体的处理者对象只能在两个行为中选择一个：
     * 一是承担责任，二是把责任推给下家。
     * 不允许出现某一个具体处理者对象在承担了一部分责任后又 把责任向下传的情况。
     * <p>
     * 在一个纯的责任链模式里面，一个请求必须被某一个处理者对象所接收；
     * 在一个不纯的责任链模式里面，一个请求可以最终不被任何接收端对象所接收。
     * <p>
     * 纯的责任链模式的实际例子很难找到，一般看到的例子均是不纯的责任链模式的实现。
     * 有些人认为不纯的责任链根本不是责任链模式，这也许是有道理的。
     * 但是在实际的系统里，纯的责任链很难找到。
     * 如果坚持责任链不纯便不是责任链模式，那么责任链模式便不会有太大意义了
     */
    public static void main(String[] args) {
        AbstractUserContextInitor haierUserContextInitor = new HaierUserContextInitor();
        AbstractUserContextInitor casarteUserContextInitor = new CasarteUserContextInitor();
        AbstractUserContextInitor tongShuaiUserContextInitor = new TongShuaiUserContextInitor();
        haierUserContextInitor.setNextProcessor(casarteUserContextInitor);
        casarteUserContextInitor.setNextProcessor(tongShuaiUserContextInitor);

        CurrentUserInfo currentUserInfo = new CurrentUserInfo();
        currentUserInfo.setAge(23);
        currentUserInfo.setDomainName("haier");

        LOGGER.info("获取得到的用户信息为:{}", JSON.toJSONString(haierUserContextInitor.process(currentUserInfo)));


        currentUserInfo.setDomainName("casarte");
        LOGGER.info("获取得到的用户信息为:{}", JSON.toJSONString(haierUserContextInitor.process(currentUserInfo)));


        currentUserInfo.setDomainName("tongshuai");
        LOGGER.info("获取得到的用户信息为:{}", JSON.toJSONString(haierUserContextInitor.process(currentUserInfo)));


        currentUserInfo.setDomainName("gea");
        LOGGER.info("获取得到的用户信息为:{}", JSON.toJSONString(haierUserContextInitor.process(currentUserInfo)));

    }
}
