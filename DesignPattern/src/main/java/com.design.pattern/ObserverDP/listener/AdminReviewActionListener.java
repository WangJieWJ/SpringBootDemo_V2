package com.design.pattern.ObserverDP.listener;

import com.design.pattern.ObserverDP.DO.BaseActionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/10 11:04
 */
@Service
public class AdminReviewActionListener implements IAdminActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminReviewActionListener.class);

    @Override
    public void actionNotify(BaseActionInfo baseActionInfo) {
        LOGGER.info("-----------------------------------------------");
        LOGGER.info("接收到超级管理员审核的行为");
        LOGGER.info("-----------------------------------------------");
    }
}
