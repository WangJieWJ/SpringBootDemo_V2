package com.java8.service;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Title: 
 * Description: RestTemplate 携带cookie信息
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/4 09:16
 */
public class RestTemplateWithCookieDemo {


	public static void main(String[] args) {
		String[] userArray = "12,2,234,2,12,55,55,2,2,2,2,22,11,4,55".split(",");
		Stream<String> userIdStream = Arrays.stream(userArray);
//		System.out.println(JSON.toJSONString(userIdStream.collect(Collectors.partitioningBy(a -> a.length()>2))));
//		System.out.println(userIdStream.collect(Collectors.joining(";","(",")")));

	}
}
