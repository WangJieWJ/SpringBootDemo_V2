package com.druid.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 17:41
 */
@ApiModel(description = "用户新增模型")
public class UserAddDTO implements Serializable {

	@ApiModelProperty(notes = "用户名", example = "王杰")
	private String userName;

	@ApiModelProperty(notes = "年龄", example = "22")
	private Integer age;

	@ApiModelProperty(notes = "手机号", example = "18363997627")
	private String phone;

	@ApiModelProperty(notes = "用户地址", example = "山东省青岛市市南区")
	private String address;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
