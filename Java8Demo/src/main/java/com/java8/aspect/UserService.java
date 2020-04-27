package com.java8.aspect;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/23 08:35
 */
@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public void saveUser() {
		LOGGER.info("正在保存用户……");
	}

	public void updateUserInfo() {
		LOGGER.info("更新用户信息……");
	}

	public void deleteUser() {
		LOGGER.info("删除用户……");
	}
}
