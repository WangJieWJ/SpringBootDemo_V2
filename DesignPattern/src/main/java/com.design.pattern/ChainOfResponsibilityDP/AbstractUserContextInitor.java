package com.design.pattern.ChainOfResponsibilityDP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 16:11
 */
public abstract class AbstractUserContextInitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserContextInitor.class);

    private AbstractUserContextInitor abstractUserContextInitor;

    public void setNextProcessor(AbstractUserContextInitor abstractUserContextInitor) {
        this.abstractUserContextInitor = abstractUserContextInitor;
    }

    protected AbstractUserContextInitor getNextProcessor() {
        return this.abstractUserContextInitor;
    }

    public UserContext process(CurrentUserInfo currentUserInfo) {
        if (!this.accept(currentUserInfo)) {
            if (this.getNextProcessor() == null) {
                LOGGER.error("找不到对应的用户信息初始化类");
                return null;
            }
            return this.getNextProcessor().process(currentUserInfo);
        }
        return this.excute(currentUserInfo);
    }

    public abstract UserContext excute(CurrentUserInfo currentUserInfo);

    public abstract boolean accept(CurrentUserInfo currentUserInfo);
}
