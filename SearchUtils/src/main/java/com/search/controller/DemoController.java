package com.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/18 10:44
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

	@GetMapping(value = "/save")
	public void save() {
		LOGGER.info("正在Controller中执行操作！！！");
	}
}
