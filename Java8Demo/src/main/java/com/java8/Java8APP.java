package com.java8;

import com.java8.aspect.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/3/1 11:25
 */
@SpringBootApplication
public class Java8APP {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Java8APP.class).web(true).run(args);
	}

	@Autowired
	private UserService userService;

	@Bean
	public CommandLineRunner aspectRunner() {
		return args -> {
//			userService.saveUser();
//			System.out.println("----------------------------------");
//			userService.updateUserInfo();
			System.out.println("----------------------------------");
			userService.deleteUser();
			System.out.println("----------------------------------");
		};
	}
}
