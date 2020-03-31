package com.java8.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-16 17:26
 */
public class AdminUser {

	/**
	 * 用户Id
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 用户年龄
	 */
	private Integer age;

	/**
	 * 用户地址
	 */
	private String address;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 用户爱好
	 */
	private List<String> hobby;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<String> getHobby() {
		return hobby;
	}

	public void setHobby(List<String> hobby) {
		this.hobby = hobby;
	}

	/**
	 * 一次设置一个爱好
	 * @param hobby 爱好
	 */
	public void addHobby(String hobby) {
		this.hobby = Optional.ofNullable(this.hobby).orElse(new ArrayList<>());
		this.hobby.add(hobby);
	}

	/**
	 * 一次设置两个爱好
	 * @param hobbyOne 爱好一
	 * @param hobbySecond 爱好二
	 */
	public void addHobby(String hobbyOne, String hobbySecond) {
		this.hobby = Optional.ofNullable(this.hobby).orElse(new ArrayList<>());
		this.hobby.add(hobbyOne);
		this.hobby.add(hobbySecond);
	}
}
