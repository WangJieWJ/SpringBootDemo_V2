package com.shiro.entity;

import java.io.Serializable;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 16:16
 */
public class ShiroUser implements Serializable {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 英文名称
	 */
	private String enUserName;

	/**
	 * 密码
	 */
	private String passWord;

	/**
	 * 盐
	 */
	private String salt;

	public ShiroUser() {
	}

	public ShiroUser(String userName, String enUserName, String passWord, String salt) {
		this.userName = userName;
		this.enUserName = enUserName;
		this.passWord = passWord;
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEnUserName() {
		return enUserName;
	}

	public void setEnUserName(String enUserName) {
		this.enUserName = enUserName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
