package com.shiro.shiro.realm;

import com.shiro.entity.ShiroUser;
import com.shiro.shiro.token.UserAuthenticationToken;
import com.shiro.shiro.utils.HashUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 14:53
 */
public class UserRealm extends AuthorizingRealm {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

	/**
	 * 认证
	 * @param authenticationToken 待认证的用户信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		if (!(authenticationToken instanceof UserAuthenticationToken)) {
			LOGGER.error("目前仅支持UserAuthenticationToken，此时的AuthenticationToken为:{}", authenticationToken.getClass());
			return null;
		}
		UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authenticationToken;
		String userName = userAuthenticationToken.getUsername();
		LOGGER.info("开始执行登录操作，当前登录用户：{}", userName);

		LOGGER.info("此处从数据中获取当前登录用户的信息");
		ShiroUser shiroUser = new ShiroUser(userName, "wangjie", HashUtils.getEncryptPassWord("123456", "222222"), "222222");
		// 此处判断用户的有效期、是否锁定等信息

		return new SimpleAuthenticationInfo(shiroUser, shiroUser.getPassWord(), ByteSource.Util.bytes(shiroUser.getSalt()), getName());
	}

	/**
	 * 授权
	 * @param principalCollection 认证通过的身份信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		// 此方法仅登录时调用一次，之后会缓存到Redis中，缓存过期之后才会执行
		ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
		LOGGER.info("开始进行授权操作，当前授权用户为:{}", shiroUser.getUserName());
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 进行权限赋值
		authorizationInfo.addStringPermission("home:system:updateUserInfo");

		return authorizationInfo;
	}

	/**
	 * 重写AuthorizationCacheKey来获取Redis中缓存的Key
	 * @param principals 认证通过的身份信息
	 */
	@Override
	protected String getAuthorizationCacheKey(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		return String.format(":%s", shiroUser.getEnUserName());
	}

	/**
	 * 重写getAuthorizationCacheName来设置Redis中缓存授权信息的Key
	 */
	@Override
	public String getAuthorizationCacheName() {
		return ":userRealm:authorization";
	}
}
