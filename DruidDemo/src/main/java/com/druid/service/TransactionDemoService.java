package com.druid.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/14 13:51
 */
@Service
public class TransactionDemoService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SiteMessageService siteMessageService;

	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction() {
		Integer currentUserId = (new Random()).nextInt(10000);
		String orderGoods = "机器猫";
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		orderService.saveUserOrder(currentUserId, orderGoods, currentTime);

		String message = String.format("%s您好,您于%s购买的%s将及时送达！", currentUserId, currentTime, orderGoods);

		try {
			siteMessageService.saveUserSiteMessage(currentUserId, message, currentTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
