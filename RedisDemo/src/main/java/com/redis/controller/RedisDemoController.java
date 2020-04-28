package com.redis.controller;

import java.util.Set;

import com.redis.service.RedisTypeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/4/27 17:08
 */
@RestController
@RequestMapping(value = "/type")
public class RedisDemoController {

	@Autowired
	private RedisTypeService redisTypeService;


	@ApiOperation(value = "接口调用限制的Demo")
	@GetMapping(value = "/apiLimitDemo")
	@ApiImplicitParam(value = "当前登录用户", name = "currentUser", required = true, paramType = "query", dataType = "String")
	public void apiLimitDemo(String currentUser) {
		System.out.println(redisTypeService.apiLimitDemo(currentUser));
	}

	@ApiOperation(value = "帖子排行榜，帖子添加评论")
	@PostMapping(value = "/threadAddComment")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "帖子Id", name = "threadId", required = true, paramType = "query", dataType = "Long"),
	})
	public void addThreadComment(Long threadId) {
		redisTypeService.addThreadComment(threadId);
	}

	@ApiOperation(value = "帖子排行榜，获取帖子排行列表")
	@PostMapping(value = "/getThreadRank")
	@ApiImplicitParam(value = "排行榜前几名", name = "topNum", required = true, paramType = "query", dataType = "Integer")
	public Set<Object> getThreadRank(Integer topNum) {
		return redisTypeService.getThreadRank(topNum);
	}

	@ApiOperation(value = "设置Redis缓存Key")
	@PostMapping(value = "/setRedisCacheKey")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "key", name = "key", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(value = "value", name = "value", required = true, paramType = "query", dataType = "String")
	})
	public void setRedisCacheKey(String key, String value) {
		redisTypeService.setRedisCacheKey(key, value);
	}

	@ApiOperation(value = "运行Lua脚本")
	@PostMapping(value = "/runLuaScript")
	public Object runLuaScript(String delKey, String delValue) {
		return redisTypeService.runLuaScript(delKey, delValue);
	}

}
