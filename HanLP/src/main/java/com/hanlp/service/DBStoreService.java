package com.hanlp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringJoiner;

import com.hanlp.constants.CustomsStructuredDataConstant;
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

	public static void updateUnique(String nerTag) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM hanlp_accuracy WHERE ner_tag = '%s'", nerTag));

		List<String> markResultList = new ArrayList<>();
		while (resultSet.next()) {
			markResultList.clear();
			boolean hasEdit = false;
			String[] mark_result = resultSet.getString("mark_result").split(";");
			StringJoiner joiner = new StringJoiner(";");
			for (int i = 0; i < mark_result.length; i++) {
				if (!markResultList.contains(mark_result[i])) {
					markResultList.add(mark_result[i]);
					joiner.add(mark_result[i]);
				} else {
					hasEdit = true;
				}
			}
			if (hasEdit) {
				System.out.println(resultSet.getInt("id"));
				connection.createStatement().execute(String.format("UPDATE hanlp_accuracy SET mark_result = '%s' WHERE id = %s", joiner.toString(), resultSet.getInt("id")));
			}
		}
	}

	/**
	 * 数据清洗
	 */
	public static void dataWish() {
		List<String> nerList = Arrays.asList(CustomsStructuredDataConstant.SeizedOrganization,
				CustomsStructuredDataConstant.SeizedLocation,
				CustomsStructuredDataConstant.DeclareGoods,
				CustomsStructuredDataConstant.RealGoods,
				CustomsStructuredDataConstant.StartCountry,
				CustomsStructuredDataConstant.EndCountry,
				CustomsStructuredDataConstant.InvolveUser,
				CustomsStructuredDataConstant.InvolveCompany);
		nerList.forEach(nerTag -> {
			try {
				updateUnique(nerTag);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void calcAccuracyAll() {
		List<String> nerList = Arrays.asList(CustomsStructuredDataConstant.SeizedOrganization,
				CustomsStructuredDataConstant.SeizedLocation,
				CustomsStructuredDataConstant.DeclareGoods,
				CustomsStructuredDataConstant.RealGoods,
				CustomsStructuredDataConstant.StartCountry,
				CustomsStructuredDataConstant.EndCountry,
				CustomsStructuredDataConstant.InvolveUser,
				CustomsStructuredDataConstant.InvolveCompany);
		nerList.forEach(nerTag -> {
			try {
				calcAccuracy(nerTag);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void calcAccuracy(String nerTag) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM hanlp_accuracy WHERE ner_tag = '%s'", nerTag));

		int markSize = 0;
		int nerSize = 0;
		int correctSize = 0;
		while (resultSet.next()) {
			String[] mark_result = resultSet.getString("mark_result").split(";");
			String[] ner_result = resultSet.getString("ner_result").split(";");
			markSize += mark_result.length;
			nerSize += ner_result.length;
			correctSize += intersect(mark_result, ner_result);
		}
		double p = correctSize / (nerSize * 1.00);
		double r = correctSize / (markSize * 1.00);
		System.out.println(nerTag + ",correctSize/markSize:" + r);
		System.out.println(nerTag + ",correctSize/nerSize:" + p);
		System.out.println(nerTag + ",F1:" + ((2 * p * r) / (r + p)));
	}

	public static int intersect(String[] arr1, String[] arr2) {
		HashMap<String, Boolean> map = new HashMap<>();
		LinkedList<String> list = new LinkedList<>();
		for (String str : arr1) {
			map.put(str, Boolean.FALSE);
		}
		for (String str : arr2) {
			if (map.containsKey(str)) {
				map.put(str, Boolean.TRUE);
			}
		}

		for (Entry<String, Boolean> e : map.entrySet()) {
			if (e.getValue().equals(Boolean.TRUE)) {
				list.add(e.getKey());
			}
		}

		return list.size();
	}


	public static void main(String[] args) {
		calcAccuracyAll();
	}

}
