package com.design.pattern.ChainOfResponsibilityDP;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/9 16:15
 */
public class CurrentUserInfo {

    /**
     * 当前登录用户的域名
     */
    private String domainName;
    /**
     * 当前登录用户的年龄
     */
    private Integer age;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
