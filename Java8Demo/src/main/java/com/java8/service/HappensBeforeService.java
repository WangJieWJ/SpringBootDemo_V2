package com.java8.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-18 10:45
 */
public class HappensBeforeService {

    public static void main(String[] args) {
//        final List<String> userNameList = new ArrayList<>();
//        userNameList.add("userName1");
//        Thread thread = new Thread(() -> {
//            userNameList.forEach(userName -> {
//                System.out.println(String.format("获取到的用户名为:%s", userName));
//            });
//        });
//        userNameList.add("userName2");
//        userNameList.add("userName3");
//        thread.start();

        final List<String> userIdList = new ArrayList<>(5);
        Thread s = new Thread(() -> {
            userIdList.add("s1111");
            userIdList.add("s2222");
        });
        Thread t = new Thread(() -> {
            try {
                s.start();
                s.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userIdList.add("t1111");
            userIdList.add("t2222");
            userIdList.forEach(userId -> {
                System.out.println(String.format("获取到的用户Id为:%s", userId));
            });
        });
        t.start();

    }
}
