package com.redis.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/30 17:54
 */
public class RedisGeoServiceTest extends AbstractTest {

	@Autowired
	private RedisGeoService redisGeoService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisGeoServiceTest.class);

	private static final String CITIY_CACHE_KEY = "city";

	@Test
	public void geoAdd() {
		LOGGER.info("保存城市经纬度成功！,成功保存数量为:{}", redisGeoService.geoAdd(CITIY_CACHE_KEY, new Point(105.840037405, 32.4377136745), "拓尔思"));
	}

	@Test
	public void geoBatchAdd() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cities.json");
		try {
			List<String> cityList = IOUtils.readLines(inputStream, "UTF-8");
			Map<String, JSONObject> map = JSONObject.parseObject(cityList.get(0), Map.class);
			Map<String, Point> batchAddMap = new HashMap<>();
			JSONObject cityInfo;
			for (Map.Entry<String, JSONObject> itemMap : map.entrySet()) {
				cityInfo = itemMap.getValue();
				batchAddMap.put(cityInfo.getString("name"), new Point(cityInfo.getDoubleValue("lon"), cityInfo.getDoubleValue("lat")));
			}
			LOGGER.info("批量保存城市经纬度成功！,成功保存数量为:{}", redisGeoService.geoBatchAdd(CITIY_CACHE_KEY, batchAddMap));
		} catch (IOException e) {
			LOGGER.error("文件读取失败", e);
		}
	}

	@Test
	public void geoPos() {
		List<Point> pointList = redisGeoService.geoPos(CITIY_CACHE_KEY, "北京市", "青岛市", "沈阳市", "拓尔思");
		for (Point point : pointList) {
			LOGGER.info("X:{},Y:{}", point.getX(), point.getY());
		}
	}

	@Test
	public void geoDist() {
		Distance distance = redisGeoService.geoDist(CITIY_CACHE_KEY, "北京市", "北京市");
		LOGGER.info("北京市和青岛市之间的距离:{},{},{}", distance.getValue(), distance.getMetric().getMultiplier(), distance.getMetric().getAbbreviation());
	}

	@Test
	public void geoRadius() {
	}

	@Test
	public void geoRadiusByMember() {
	}

	@Test
	public void geoHash() {
	}

	@Test
	public void redisScan() {
		long startTime = System.currentTimeMillis();
		Object result = redisGeoService.redisScan("bbsIdsUserId_*");
		System.out.println(((Set<String>) result).size());
		System.out.println((System.currentTimeMillis() - startTime) / 1000);
	}
}