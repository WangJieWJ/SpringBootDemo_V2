package com.hanlp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/31 15:21
 */
public class DBStoreService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBStoreService.class);

	private static Connection connection;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/local_db?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull", "root", "123456");
		} catch (Exception e) {
			LOGGER.error("执行报错！", e);
		}
	}

	/**
	 * 数据保存
	 */
	public static void saveOriginData(String insertSql) throws SQLException {
		Statement statement = connection.createStatement();
		System.out.println(String.format("数据保存结果:%b", statement.execute(insertSql))); ;
	}

}
