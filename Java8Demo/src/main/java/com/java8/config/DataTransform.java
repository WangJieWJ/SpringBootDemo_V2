package com.java8.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-05 16:44
 */
public class DataTransform {

	public static void main(String[] args) throws IOException {
		File file = new File("/Users/wangjie/Development/项目/海关/《内部风险信息》（2019年第53期）.docx");
		List<String> docDataList = IOUtils.readLines(new FileInputStream(file), Charset.forName("UTF-8"));
		for (String docData : docDataList) {
			System.out.println(docData);
		}
	}
}
