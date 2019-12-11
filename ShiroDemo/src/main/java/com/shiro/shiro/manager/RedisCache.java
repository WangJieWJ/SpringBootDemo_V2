package com.shiro.shiro.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * Title: 
 * Description: 配置自定义的Shiro权限缓存
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 18:13
 */
public class RedisCache<K, V> implements Cache<K, V> {

	/**
	 * 缓存Key的前缀
	 */
	private static final String cachePrefix = "shiro:cache:";

	/**
	 * 缓存30分钟
	 */
	private static final int timeout = 30 * 60;

	private String cacheName;

	private RedisTemplate<String,Object> redisTemplate;

	public RedisCache(String cacheName, RedisTemplate<String,Object> redisTemplate) {
		this.cacheName = cacheName;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object get(Object key) throws CacheException {
		return redisTemplate.opsForValue().get(buildCacheKey(key));
	}

	@Override
	public Object put(Object key, Object value) throws CacheException {
		String cacheKey = buildCacheKey(key);
		Object preValue = redisTemplate.opsForValue().get(cacheKey);
		redisTemplate.opsForValue().set(cacheKey, value, timeout, TimeUnit.SECONDS);
		return preValue;
	}

	@Override
	public Object remove(Object key) throws CacheException {
		String cacheKey = buildCacheKey(key);
		Object preValue = redisTemplate.opsForValue().get(cacheKey);
		redisTemplate.delete(cacheKey);
		return preValue;
	}

	@Override
	public void clear() throws CacheException {
		Set<String> cacheKeySet = keys();
		if (!CollectionUtils.isEmpty(cacheKeySet)) {
			cacheKeySet.stream().peek(cacheKey -> redisTemplate.delete(cacheKey)).count();
		}
	}

	@Override
	public int size() {
		Set<String> cacheKeySet = keys();
		return CollectionUtils.isEmpty(cacheKeySet) ? 0 : cacheKeySet.size();
	}

	@Override
	public Set keys() {
		String cacheKeyPattern = buildCacheKey("*");
		Set<String> cacheKeySet = redisTemplate.keys(cacheKeyPattern);
		return CollectionUtils.isEmpty(cacheKeySet) ? Collections.emptySet() : Collections.unmodifiableSet(cacheKeySet);
	}

	@Override
	public Collection values() {
		Set<String> cacheKeySet = keys();
		Collection values = Collections.EMPTY_SET;
		if (!CollectionUtils.isEmpty(cacheKeySet)) {
			cacheKeySet.stream().peek(cacheKey -> values.add(redisTemplate.opsForValue().get(cacheKey))).count();
		}
		return values;
	}

	@Override
	public String toString() {
		return String.format("RedisCache，cacheName:%s，cacheSize:%s", this.cacheName, size());
	}

	/**
	 * 获取缓存Key
	 */
	public String buildCacheKey(Object key) {
		String resultKey = "";
		if (key instanceof String) {
			resultKey = key.toString();
		}
		else {
			resultKey = JSON.toJSONString(key);
		}
		return String.format("%s%s%s", cachePrefix, cacheName, resultKey);
	}
}
