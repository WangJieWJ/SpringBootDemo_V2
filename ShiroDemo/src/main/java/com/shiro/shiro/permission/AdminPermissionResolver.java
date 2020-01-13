package com.shiro.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-25 15:14
 */
public class AdminPermissionResolver implements PermissionResolver {
	@Override
	public Permission resolvePermission(String permissionString) {
		return new AdminPermission(permissionString);
	}
}
