package com.shiro.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import com.shiro.shiro.credential.UserPassWordCredentialMatcher;
import com.shiro.shiro.filter.LoginCheckFilter;
import com.shiro.shiro.manager.RedisCacheManager;
import com.shiro.shiro.manager.RedisCacheSessionDAO;
import com.shiro.shiro.realm.UserRealm;
import com.shiro.shiro.utils.HashUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Title: 
 * Description: shiro配置类
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 14:31
 */
@Configuration
public class ShiroConfig {

	@Autowired
	private RedisTemplate redisTemplate;

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 设置securityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager());

		// 登录地址
		shiroFilterFactoryBean.setLoginUrl("");
		// 登录成功跳转地址
		shiroFilterFactoryBean.setSuccessUrl("");
		// 未授权跳转地址
		shiroFilterFactoryBean.setUnauthorizedUrl("");

		// 配置自定义的Filter
		Map<String, Filter> selfFilter = new HashMap<>(2);
		selfFilter.put("loginCheck", loginCheckFilter());
		shiroFilterFactoryBean.setFilters(selfFilter);

		// 配置路径拦截
		Map<String, String> filterChainDefinitionMap = new HashMap<>(2);
		// 配置所有路径需要使用两个**
		filterChainDefinitionMap.put("/**", "loginCheck");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
		// 设置自定义的Realm，支持配置多个Realm
		defaultSecurityManager.setRealm(userRealm());
		// 此处可以设置通用的CacheManager，将会覆盖掉所有的Realm的CacheManager
		// defaultSecurityManager.setCacheManager();
		// 设置session缓存，实现集群session共享
		defaultSecurityManager.setSessionManager(defaultWebSessionManager());
		return defaultSecurityManager;
	}

	@Bean
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setSessionDAO(redisCacheSessionDAO());
		return defaultWebSessionManager;
	}

	@Bean
	public RedisCacheSessionDAO redisCacheSessionDAO() {
		RedisCacheSessionDAO redisCacheSessionDAO = new RedisCacheSessionDAO(redisTemplate);
		return redisCacheSessionDAO;
	}

	@Bean
	public UserRealm userRealm() {
		UserRealm userRealm = new UserRealm();
		// 配置自己的Realm
		// 匹配自定义的密码校验策略
		userRealm.setCredentialsMatcher(userPassWordCredentialMatcher());
		// 设置为自定义的Shiro缓存管理器
		userRealm.setCachingEnabled(true);
		userRealm.setCacheManager(cacheManager());
		// AuthenticationInfo(用户的认证信息)是否需要缓存
		userRealm.setAuthenticationCachingEnabled(false);
		// AuthorizationInfo(用户的授权信息)是否需要缓存
		userRealm.setAuthorizationCachingEnabled(true);
		return userRealm;
	}

	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		return redisCacheManager;
	}

	@Bean
	public UserPassWordCredentialMatcher userPassWordCredentialMatcher() {
		UserPassWordCredentialMatcher userPassWordCredentialMatcher = new UserPassWordCredentialMatcher();
		// 散列算法:这里使用MD5算法
		userPassWordCredentialMatcher.setHashAlgorithmName(HashUtils.ALGORITHM_NAME);
		// 散列的次数，比如散列两次，相当于md5(md5(""))
		userPassWordCredentialMatcher.setHashIterations(HashUtils.HASH_ITERATIONS);
		// 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64
		userPassWordCredentialMatcher.setStoredCredentialsHexEncoded(true);
		return userPassWordCredentialMatcher;
	}

	@Bean
	public LoginCheckFilter loginCheckFilter() {
		LoginCheckFilter loginCheckFilter = new LoginCheckFilter("未登录跳转地址");
		return loginCheckFilter;
	}
}
