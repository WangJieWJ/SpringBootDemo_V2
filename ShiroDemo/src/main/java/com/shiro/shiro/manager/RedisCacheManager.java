package com.shiro.shiro.manager;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 18:05
 */
public class RedisCacheManager implements CacheManager {


	private RedisTemplate<String, Object> redisTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheManager.class);

	public RedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		LOGGER.info("获取缓存的Key为[{}]", name);
		return new RedisCache<>(name, redisTemplate);
	}
}
