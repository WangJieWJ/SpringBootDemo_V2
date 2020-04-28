package com.redis.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * Title: Redis 数据类型测试
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/27 17:10
 */
@Service
public class RedisTypeService {

	private RedisTemplate redisTemplate;

	public RedisTypeService(RedisTemplate redisTemplate) {
		RedisSerializer redisKeySerializer = new StringRedisSerializer();
		RedisSerializer redisValueSerializer = new Jackson2JsonRedisSerializer(Object.class);
		redisTemplate.setKeySerializer(redisKeySerializer);
		redisTemplate.setValueSerializer(redisValueSerializer);
		redisTemplate.setHashKeySerializer(redisKeySerializer);
		redisTemplate.setHashValueSerializer(redisValueSerializer);
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 基于zset实现时间窗口 -- 接口调用限制
	 * 1分钟超过5次接口限制
	 * @param currentUser 当前登录用户
	 * @return 是否允许继续调用接口
	 */
	public boolean apiLimitDemo(String currentUser) {
		Date currentDate = new Date();
		String redisKey = String.format("apiLimit:%s", currentUser);
		long beforeOneMinute = currentDate.getTime() - 60 * 1000;
		// 首先删除 1分钟 之前的数据
		redisTemplate.boundZSetOps(redisKey).removeRangeByScore(0, beforeOneMinute);
		// 如果一分钟之内的数据超过5条，就限制接口调用
		if (redisTemplate.boundZSetOps(redisKey).zCard() >= 5) {
			return false;
		}
		boolean addResult = redisTemplate.boundZSetOps(redisKey).add(currentDate.getTime(), currentDate.getTime());
		System.out.println(String.format("接口调用时间：%s,调用记录添加结果：%b", currentDate, addResult));
		return true;
	}

	/**
	 * 基于zset实现的 -- 某段时间内的排行榜
	 * 某个帖子评论数添加1
	 * @param threadId 标签Id
	 */
	public void addThreadComment(Long threadId) {
		String cacheKey = getCacheKey();
		Double incrResult = redisTemplate.boundZSetOps(cacheKey).incrementScore(threadId, 1);
		// 5分钟之后失效
		redisTemplate.boundZSetOps(cacheKey).expire(5, TimeUnit.MINUTES);
		System.out.println(String.format("帖子%d添加评论成功:%s", threadId, incrResult));
	}

	/**
	 * 基于zset实现的 -- 某段时间内的排行榜
	 * 获取当前 前五分钟内 评论前topNum 的帖子
	 * @param topNum
	 * @return
	 */
	public Set<Object> getThreadRank(Integer topNum) {
		String topNumResult = "thread:commentNum:topNum";
		redisTemplate.boundZSetOps(getCacheKey()).unionAndStore(getRecentCacheKey(), topNumResult);
		return redisTemplate.boundZSetOps(topNumResult).reverseRange(0, topNum);
	}

	/**
	 * 获取最近5分钟之内的缓存Key
	 */
	private Collection<String> getRecentCacheKey() {
		List cacheKeyList = Lists.newArrayList();
		long currentTime = getCurrentMinutes();
		for (int i = 1; i < 5; i++) {
			cacheKeyList.add(String.format("thread:commentNum:%d", (currentTime - i)));
		}
		return cacheKeyList;
	}


	private String getCacheKey() {
		return String.format("thread:commentNum:%d", getCurrentMinutes());
	}

	private long getCurrentMinutes() {
		return new Date().getTime() / (60 * 1000);
	}

	/**
	 * 设置Redis缓存Key
	 * @param key 缓存项
	 * @param value 缓存值
	 */
	public void setRedisCacheKey(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 运行Lua 脚本
	 */
	public Object runLuaScript(String delKey, String delValue) {
		String scriptStr = "if redis.call('get',KEYS[1]) == ARGV[1] then "
				+ " 	return redis.call('del',KEYS[1])"
				+ " else "
				+ "		return 0 "
				+ "	end";
		RedisScript redisScript = new DefaultRedisScript(scriptStr, Integer.class);
		return redisTemplate.execute(redisScript, Lists.newArrayList(delKey), delValue);
	}

}
