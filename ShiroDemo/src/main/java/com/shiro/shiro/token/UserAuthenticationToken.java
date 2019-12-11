package com.shiro.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Title: 
 * Description: 自定义认证信息载体，可以来做免登操作
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 15:46
 */
public class UserAuthenticationToken extends UsernamePasswordToken {

	/**
	 * 是否校验密码，密码登录需要校验，免登不需要简要密码
	 */
	private boolean checkPassWord;

	public UserAuthenticationToken() {
	}

	public UserAuthenticationToken(final String username, final String password) {
		this(username, password, false, null, true);
	}

	public UserAuthenticationToken(final String username, final String password, final boolean checkPassWord) {
		this(username, password, false, null, checkPassWord);
	}

	public UserAuthenticationToken(final String username, final String password, final boolean rememberMe, final String host, final boolean checkPassWord) {
		super(username, password, rememberMe, host);
		this.checkPassWord = checkPassWord;
	}

	public boolean isCheckPassWord() {
		return checkPassWord;
	}

	public void setCheckPassWord(boolean checkPassWord) {
		this.checkPassWord = checkPassWord;
	}
}
