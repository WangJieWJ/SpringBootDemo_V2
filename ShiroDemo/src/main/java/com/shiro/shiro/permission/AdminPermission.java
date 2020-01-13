package com.shiro.shiro.permission;

import java.io.Serializable;
import java.util.Objects;

import org.apache.shiro.authz.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-25 15:14
 */
public class AdminPermission implements Permission, Serializable {

	// 权限字符串
	private String permissionString;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminPermission.class);

	public AdminPermission(String permissionString) {
		this.permissionString = permissionString;
	}

	public String getPermissionString() {
		return permissionString;
	}

	public void setPermissionString(String permissionString) {
		this.permissionString = permissionString;
	}

	@Override
	public boolean implies(Permission p) {
		if (!(p instanceof AdminPermission)) {
			LOGGER.error("暂不支持除AdminPermission类型之外的权限判断，当前权限类型:{}", p.getClass());
			return false;
		}
		AdminPermission newAdminPermission = (AdminPermission) p;
		return Objects.equals(this.permissionString, newAdminPermission.getPermissionString());
	}

}
