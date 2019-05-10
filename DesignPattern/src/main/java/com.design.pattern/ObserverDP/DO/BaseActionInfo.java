package com.design.pattern.ObserverDP.DO;

import java.util.Date;

/**
 * Title:
 * Description: 行为 基类  详情
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/10 10:48
 */
public class BaseActionInfo {

    /**
     * 行为类型 具体交给谁去处理该行为
     */
    private String actionType;
    /**
     * 行为类型 创建时间
     */
    private Date actionCreateTime;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionCreateTime() {
        return actionCreateTime;
    }

    public void setActionCreateTime(Date actionCreateTime) {
        this.actionCreateTime = actionCreateTime;
    }
}
