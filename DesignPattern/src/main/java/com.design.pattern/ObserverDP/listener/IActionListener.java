package com.design.pattern.ObserverDP.listener;

import com.design.pattern.ObserverDP.DO.BaseActionInfo;

/**
 * Title:
 * Description: 观察者原型
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/10 09:36
 */
public interface IActionListener {

    void actionNotify(BaseActionInfo baseActionInfo);

}
