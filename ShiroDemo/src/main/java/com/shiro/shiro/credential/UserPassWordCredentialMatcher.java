package com.shiro.shiro.credential;

import com.shiro.shiro.token.UserAuthenticationToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 自定义密码匹配校验
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 16:28
 */
public class UserPassWordCredentialMatcher extends HashedCredentialsMatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPassWordCredentialMatcher.class);

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		if (!(token instanceof UserAuthenticationToken)) {
			LOGGER.error("目前仅支持UserAuthenticationToken的权限认证");
			return false;
		}
		UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) token;
		if (!userAuthenticationToken.isCheckPassWord()) {
			// 不需要校验密码
			return true;
		}
		Object tokenHashedCredentials = hashProvidedCredentials(token, info);
		Object accountCredentials = getCredentials(info);
		return equals(tokenHashedCredentials, accountCredentials);
	}
}
