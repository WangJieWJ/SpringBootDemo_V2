package com.rabbitmq.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Title:
 * Description: 时间工具类
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/16 16:52
 */
public class DateUtils {

    public static final String DATE_FORMAT_STR_1 = "YYYY-MM-SS HH:mm:ss";
    public static final String DATE_FORMAT_STR_2 = "YYYY年MM月SS日HH时mm分ss秒";
    public static final String DATE_FORMAT_STR_3 = "YYYYMMSSHHmmss";

    /**
     * 获取格式化之后的时间字符串
     *
     * @param date          时间
     * @param dateFormatStr 日期格式
     */
    public static String getFormatDateStr(Date date, String dateFormatStr) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
        return format.format(date);
    }
}
