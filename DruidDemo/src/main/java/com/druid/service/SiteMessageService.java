package com.druid.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
public class SiteMessageService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = FileNotFoundException.class)
	public void saveUserSiteMessage(Integer userId, String message, String orderTime) throws FileNotFoundException {
		String saveSiteMessage = String.format("INSERT INTO site_message(`user_id`,`message`,`send_time`) VALUES(%s,'%s','%s')", userId, message, orderTime);
		jdbcTemplate.execute(saveSiteMessage);
	}
}
