package com.concurrent.service;

import java.time.*;
import java.util.Date;

/**
 * Title:
 * Description: Java8中的时间处理学习
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/5/27 16:34
 */
public class InstantDemo {


    public static void instantTest() {
        Instant instant = Instant.now();
        System.out.println(instant);
    }

    public static void localDateTest() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        LocalDate localDate1 = LocalDate.of(2018, 10, 2);
        System.out.println(localDate1);
        LocalDate localDate2 = localDate1.plusDays(2);
        System.out.println(localDate2);

        MonthDay monthDay = MonthDay.now();
        MonthDay monthDay1 = MonthDay.of(5, 27);
        System.out.println(monthDay.equals(monthDay1));

        YearMonth yearMonth = YearMonth.now();
        YearMonth yearMonth1 = YearMonth.of(2019, 5);
        System.out.println(yearMonth.equals(yearMonth1));

        LocalDate localDate3 = LocalDate.parse("2019-10-03");
        System.out.println(localDate3);
    }

    public static void localTimeTest() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        LocalTime localTime1 = LocalTime.of(23, 22, 11);
        System.out.println(localTime1);
    }

    public static void localDateTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
    }


    public static void main(String[] args) {
        instantTest();
        System.out.println("---------------------");
        localDateTest();
        System.out.println("---------------------");
        localTimeTest();
        System.out.println("---------------------");
        localDateTimeTest();
        //Date与Instant的相互转化
        Instant instant = Instant.now();
        Date date = Date.from(instant);
        System.out.println("---------------------");
        System.out.println(date);
        System.out.println("---------------------");
        Instant instant2 = date.toInstant();
        System.out.println(instant2);
        System.out.println("---------------------");

        // Date转为LocalDateTime
        Date date2 = new Date();
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        System.out.println(localDateTime2);
        System.out.println("---------------------");

        // LocalDateTime转Date
        LocalDateTime localDateTime3 = LocalDateTime.now();
        Instant instant3 = localDateTime3.atZone(ZoneId.systemDefault()).toInstant();
        Date date3 = Date.from(instant);
        System.out.println(date3);
        System.out.println("---------------------");

        // LocalDate转Date
        // 因为LocalDate不包含时间，所以转Date时，会默认转为当天的起始时间，00:00:00
        LocalDate localDate4 = LocalDate.now();
        Instant instant4 = localDate4.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date4 = Date.from(instant);
        System.out.println("---------------------");
    }

}
