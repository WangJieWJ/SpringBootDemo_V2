package com.design.pattern.ObserverDP;

import com.design.pattern.ObserverDP.DO.BaseActionInfo;
import com.design.pattern.ObserverDP.listener.IActionListener;
import com.design.pattern.ObserverDP.listener.IAdminActionListener;
import com.design.pattern.ObserverDP.listener.IUserActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Title:
 * Description: 观察者模式 测试
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/10 09:32
 */
@Component
public class ObserverTest implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(ObserverTest.class);

    //所有行为监听者
    private static Collection<IActionListener> actionListeners;
    //所有用户行为监听者
    private Collection<IUserActionListener> userActionListeners;
    //所有超级管理员行为监听者
    private Collection<IAdminActionListener> adminActionListeners;

    @Override
    public void run(String... strings) throws Exception {
        init();

        BaseActionInfo baseActionInfo = new BaseActionInfo();
        baseActionInfo.setActionCreateTime(new Date());
        baseActionInfo.setActionType("Hello World!");

        notifyAllAction(baseActionInfo);
        notifyAllUserAction(baseActionInfo);
        notifyAllAdminAction(baseActionInfo);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void init() {
        LOGGER.info("开始获取所有的行为监听者");
        actionListeners = new LinkedList<>(this.applicationContext.getBeansOfType(IActionListener.class).values());

        LOGGER.info("开始获取所有的用户行为监听者");
        userActionListeners = new LinkedList<>(this.applicationContext.getBeansOfType(IUserActionListener.class).values());

        LOGGER.info("开始获取所有的超级管理员行为监听者");
        adminActionListeners = new LinkedList<>(this.applicationContext.getBeansOfType(IAdminActionListener.class).values());
    }

    /**
     * 通知所有的行为监听者
     */
    private void notifyAllAction(BaseActionInfo baseActionInfo) {
        for (IActionListener actionListener : actionListeners) {
            actionListener.actionNotify(baseActionInfo);
        }
    }

    /**
     * 通知所有的用户行为监听者
     */
    private void notifyAllUserAction(BaseActionInfo baseActionInfo) {
        for (IUserActionListener userActionListener : userActionListeners) {
            userActionListener.actionNotify(baseActionInfo);
        }
    }

    /**
     * 通知所有的超级管理员行为监听者
     */
    private void notifyAllAdminAction(BaseActionInfo baseActionInfo) {
        for (IAdminActionListener adminActionListener : adminActionListeners) {
            adminActionListener.actionNotify(baseActionInfo);
        }
    }
}
