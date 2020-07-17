package com.druid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * Create Time:2020/7/14 13:41
 */
@Service
public class OrderService {


	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveUserOrder(Integer userId, String orderGoods, String orderTime) {
		String saveUserOrder = String.format("INSERT INTO user_order(`user_id`,`order_goods`,`order_time`) VALUES(%s,'%s','%s')", userId, orderGoods, orderTime);
		jdbcTemplate.execute(saveUserOrder);
	}

}
