package com.redis.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/30 16:43
 */
@Service
public class RedisGeoService {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 增加地址位置的坐标
	 */
	public Long geoAdd(String key, Point point, String memberName) {
		return redisTemplate.opsForGeo().geoAdd(key, point, memberName);
	}

	/**
	 * 批量添加地理位置
	 */
	public Long geoBatchAdd(String key, Map<String, Point> memberPointMap) {
		return redisTemplate.opsForGeo().geoAdd(key, memberPointMap);
	}

	/**
	 * 获取指定member的经纬度信息，可以指定多个member，批量返回
	 */
	public List<Point> geoPos(String key, String... members) {
		return redisTemplate.opsForGeo().geoPos(key, members);
	}

	/**
	 * 返回两个地方的距离，可以指定单位，比如米m，千米KM，英里mi，英尺ft
	 */
	public Distance geoDist(String key, String member1, String member2) {
		return redisTemplate.opsForGeo().geoDist(key, member1, member2, Metrics.KILOMETERS);
	}

	/**
	 * 返回指定半径内的按照升序排序的最多returnNum条数据
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String key, double limitedDistance, Point point, long returnNum) {
		Distance distance = new Distance(limitedDistance, Metrics.KILOMETERS);
		Circle circle = new Circle(point, distance);
		RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
		// 返回的是
		geoRadiusCommandArgs.includeCoordinates();
		// 返回数据量
		geoRadiusCommandArgs.limit(returnNum);
		// 排序方式
		geoRadiusCommandArgs.sortAscending();
		return redisTemplate.opsForGeo().geoRadius(key, circle, geoRadiusCommandArgs);
	}

	/**
	 * 按照member 返回指定半径内的按照升序排序的最多returnNum条数据
	 */
	public void geoRadiusByMember(String key, String memberName, double limitedDistance, long returnNum) {
		Distance distance = new Distance(limitedDistance, Metrics.KILOMETERS);
		RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
		// 返回的是
		geoRadiusCommandArgs.includeCoordinates();
		// 返回数据量
		geoRadiusCommandArgs.limit(returnNum);
		// 排序方式
		geoRadiusCommandArgs.sortAscending();
		redisTemplate.opsForGeo().geoRadiusByMember(key, memberName, distance, geoRadiusCommandArgs);
	}

	/**
	 * 返回某几个member的geohash集合
	 */
	public List<String> geoHash(String key, String... members) {
		return redisTemplate.opsForGeo().geoHash(key, members);
	}

	public Object redisScan(String pattem) {
		return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {

//			MultiKeyCommands multiKeyCommands = (MultiKeyCommands) connection.getNativeConnection();
//			ScanParams scanParams = new ScanParams();
//			scanParams.match(pattem);
//			scanParams.count(1000);
//			Set<String> deleteKey = new HashSet<>();
//			ScanResult<String> scan = multiKeyCommands.scan("0", scanParams);
//			while (null != scan.getStringCursor()) {
//				System.out.println("执行完一次" + scan.getResult().size());
//				deleteKey.addAll(scan.getResult());
//				if (!StringUtils.equals("0", scan.getStringCursor())) {
//					scan = multiKeyCommands.scan(scan.getStringCursor(), scanParams);
//				} else {
//					break;
//				}
//			}
//			return deleteKey;

			Set<String> binaryKeys = new HashSet<>();
			Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(pattem).count(1000).build());
			while (cursor.hasNext()) {
				byte[] key = cursor.next();
				binaryKeys.add(new String(key));
				System.out.println(String.format("cursorId:%s;position:%s;key:%s", cursor.getCursorId(), cursor.getPosition(), new String(key, StandardCharsets.UTF_8)));
			}
			System.out.println("执行完一次！！");
			try {
				cursor.close();
			} catch (IOException e) {
				// do something meaningful
			}
			return binaryKeys;
		});
	}

}
