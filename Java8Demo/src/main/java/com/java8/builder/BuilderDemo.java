package com.java8.builder;

import java.util.Date;
import java.util.function.Consumer;

import com.alibaba.fastjson.JSON;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-16 17:37
 */
public class BuilderDemo {

	public static void main(String[] args) {
		AdminUser adminUser = BuilderFactory.of(AdminUser::new)
				.with(AdminUser::setAddress, "地理位置！")
				.with(AdminUser::setAge, 23)
				.with(AdminUser::setCreateTime, new Date())
				.with(AdminUser::setUserId, 222L)
				.with(AdminUser::setUserName, "HelloWorld!")
				.with(AdminUser::addHobby, "hobby1")
				.with(AdminUser::addHobby, "hobby2_1", "hobby2_1")
				.build();
		System.out.println(JSON.toJSONString(adminUser));

		Consumer<AdminUser> c = adminUser2 -> System.out.println(adminUser2.getAddress());
		c.accept(adminUser);
	}
}
