package com.druid;

import com.druid.service.TransactionDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/7/14 12:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DruidApp.class)
public class DruidAppTest {


	@Autowired
	private TransactionDemoService transactionDemoService;

	@Test
	public void test1() {
		transactionDemoService.transaction();
	}

}