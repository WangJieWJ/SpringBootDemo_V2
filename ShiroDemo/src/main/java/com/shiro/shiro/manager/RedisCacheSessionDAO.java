package com.shiro.shiro.manager;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * Title: 
 * Description: 自定义实现Session操作DAO
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-11 10:09
 */
public class RedisCacheSessionDAO extends CachingSessionDAO {

	private RedisCache<String, Session> redisCache;

	private static final String RedisCacheSession = ":session";

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheSessionDAO.class);

	public RedisCacheSessionDAO(RedisTemplate<String, Object> redisTemplate) {
		redisCache = new RedisCache<>(RedisCacheSession, redisTemplate);
	}

	@Override
	protected void doUpdate(Session session) {
		String sessionId = getSessionId(session);
		if (StringUtils.isEmpty(sessionId)) {
			return;
		}
		session.setTimeout(RedisCache.getTimeout() * 1000);
		redisCache.put(sessionId, session);
	}

	@Override
	protected void doDelete(Session session) {
		String sessionId = getSessionId(session);
		if (StringUtils.isEmpty(sessionId)) {
			return;
		}
		redisCache.remove(sessionId);
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		session.setTimeout(RedisCache.getTimeout() * 1000);
		redisCache.put(getSessionCacheKey(sessionId), session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return null;
		}
		return (Session) redisCache.get(getSessionCacheKey(sessionId));
	}

	/**
	 * 获取当前session的Id
	 */
	private String getSessionId(Session session) {
		if (session == null || session.getId() == null) {
			LOGGER.error("当前session不存在！");
			return "";
		}
		return getSessionCacheKey(session.getId());
	}

	private String getSessionCacheKey(Serializable sessionId) {
		return String.format(":%s", sessionId);
	}
}
