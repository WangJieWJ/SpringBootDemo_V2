package com.java8.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/3/1 11:28
 */
@Service
public class FutureDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureDemoService.class);

    private static void future1() throws ExecutionException, InterruptedException {
        LOGGER.info("开始提交异步任务");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> getProductPrize(222));
        LOGGER.info("开始等待计算结果");
        LOGGER.info("获取计算结果:{}", completableFuture.get());
    }

    private static void future2() {
        List<String> shopList = Arrays.asList("特步", "耐克", "鸿星尔克", "德尔惠");
        shopList.stream().map(FutureDemoService::getShopDetailName)
                .filter(shopDetailName -> shopDetailName.length() > 12)
                .forEach(shopDetailName -> LOGGER.info("店铺详细地址:{}", shopDetailName));
    }

    private static void future3() {
        List<Integer> prizeList = Arrays.asList(2, 201, 490, 53, 97, 228);
        prizeList.stream().parallel().map(prize -> CompletableFuture.supplyAsync(() -> getProductPrize(prize)))
//                .map(future -> future.thenApply())
                .forEach(shopDetailName -> {
                    try {
                        LOGGER.info("店铺详细地址:{}", shopDetailName.get());
                    } catch (Exception e) {
                        LOGGER.error("异步执行失败!", e);
                    }
                });
    }

    private static int getProductPrize(Integer seed) {
        int returnNum = new Random().nextInt(seed);
        try {
            Thread.sleep(returnNum);
        } catch (Exception e) {
            LOGGER.info("获取产品价格失败！", e);
        }
        return returnNum;
    }

    private static String getShopDetailName(String shopName) {
        String shopDetailName = "";
        switch (shopName) {
            case "特步":
                shopDetailName = "where is 特步!";
                break;
            case "耐克":
                shopDetailName = "where is 耐克!";
                break;
            case "鸿星尔克":
                shopDetailName = "where is 鸿星尔克!";
                break;
            case "德尔惠":
                shopDetailName = "where is 德尔惠!";
                break;
            default:
                shopDetailName = "你说的啥，我也不知道！";
        }
        return shopDetailName;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        future1();
//        future2();
//        future3();

        String fileName = "";
        String logLine = "";
        String traceId = "";
        String globalcookie = "";
        String msgContent = "";
        String sessioncookie = "";
        String loginUserId = "";
        String path = "";
        String host = "";
        String classpath = "";
        String logtime = "";
        String requestIp = "";
        String requestService = "";
        JSONObject jsonObject = JSONObject.parseObject("{\"fileName\":\"SqlReporter.java\",\"logLine\":55,\"@timestamp\":\"2019-03-18T15:59:54.699Z\",\"msgContent\":\"\\nSQL：SELECT * from productneedupdate where `status`=1  ORDER BY CrTime desc\\nParams：null\\n--------------------------------------------------------------------------------\",\"path\":\"/Users/wangjie/Development/ELK/datacenter/dataCenterTest.log\",\"host\":\"wangjiedeMacBook-Pro.local\",\"classpath\":\"com.season.core.db.SqlReporter\",\"logtime\":\"2019-03-18 23:59:54.699\"}");
        if (jsonObject != null) {
            fileName = jsonObject.getString("fileName");
            logLine = jsonObject.getString("logLine");
            traceId = jsonObject.getString("traceId");
            globalcookie = jsonObject.getString("globalcookie");
            msgContent = jsonObject.getString("msgContent");
            sessioncookie = jsonObject.getString("sessioncookie");
            loginUserId = jsonObject.getString("loginUserId");
            path = jsonObject.getString("path");
            host = jsonObject.getString("host");
            classpath = jsonObject.getString("classpath");
            logtime = jsonObject.getString("logtime");
            requestIp = jsonObject.getString("requestIp");
            requestService = jsonObject.getString("requestService");
        }
        LOGGER.info("fileName:{},logLine:{},traceId{},globalcookie:{},msgContent:{},sessioncookie:{},loginUserId:{},path:{},host:{},classpath:{},logtime:{},requestIp:{},requestService:{}",
                fileName, logLine, traceId, globalcookie, msgContent, sessioncookie, loginUserId, path, host, classpath, logtime, requestIp, requestService);
    }
}
