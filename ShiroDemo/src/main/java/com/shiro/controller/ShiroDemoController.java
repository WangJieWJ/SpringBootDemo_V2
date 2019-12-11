package com.shiro.controller;

import com.alibaba.fastjson.JSON;
import com.shiro.entity.ShiroUser;
import com.shiro.shiro.token.UserAuthenticationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 14:37
 */
@RestController
@RequestMapping(value = "/rights")
@Api(value = "权限测试")
public class ShiroDemoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroDemoController.class);

	@PostMapping(value = "/check")
	@ApiOperation(value = "check", notes = "shiro权限校验")
	public void checkRights() {
		Subject subject = SecurityUtils.getSubject();

		subject.login(new UserAuthenticationToken("WangJie", "123456"));

		ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
		LOGGER.info("当前登录用户的信息为:{}", JSON.toJSONString(shiroUser));

		LOGGER.info("是否登录认证成功:{}", subject.isAuthenticated());
		String permissionStr = "home:system:updateUserInfo";

		LOGGER.info("当前登录用户是否存在:{},权限字符串:{}",
				subject.isPermitted(permissionStr), permissionStr);

	}
}
