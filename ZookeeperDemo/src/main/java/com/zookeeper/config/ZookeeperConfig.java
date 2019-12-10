package com.zookeeper.config;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.alibaba.fastjson.JSONObject;

/**
 * Title:
 * Description: zookeeper配置信息
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/7 21:02
 */
public class ZookeeperConfig {

    public static void main(String[] args) {
        // 2、读取message字段，并解析
        String log_time = "";
        String thread_name = "";
        String application_name = "";
        String platform = "";
        String service_type = "";
        String key_word = "";
        String request_url = "";
        String request_param = "";
        String request_type = "";
        String request_time = "";
        String response_content = "";
        String status_code = "";
        String is_success = "";
        String consume_time = "";
        String exception_msg = "";
        String remark = "";

        // 3、调用fastjson解析
        try {
            String messageValue = "";
            JSONObject jsonObject = JSONObject.parseObject(messageValue);
            if (jsonObject != null) {
                log_time = jsonObject.getString("logtime");
                if ("".equals(log_time)) {
                    return;
                }
                thread_name = jsonObject.getString("thread_name");
                application_name = jsonObject.getString("application_name");
                platform = jsonObject.getString("platform");
                service_type = jsonObject.getString("service_type");
                key_word = jsonObject.getString("key_word");
                request_url = jsonObject.getString("request_url");
                request_param = jsonObject.getString("request_param");
                if (request_param != null && !"".equals(request_param)) {
                    request_param = filterOffUtf8Mb4(request_param).replaceAll("\n", "")
                            .replaceAll("\r", "")
                            .replaceAll("\t", "")
                            .replaceAll("\b", "");
                }
                request_type = jsonObject.getString("request_type");
                request_time = jsonObject.getString("request_time");
                response_content = jsonObject.getString("response_content");
                if (response_content != null && !"".equals(response_content)) {
                    response_content = filterOffUtf8Mb4(response_content).replaceAll("\n", "")
                            .replaceAll("\r", "")
                            .replaceAll("\t", "")
                            .replaceAll("\b", "");
                }
                status_code = jsonObject.getString("status_code");
                is_success = jsonObject.getString("is_success");
                consume_time = jsonObject.getString("status_code");
                exception_msg = jsonObject.getString("exception_msg");
                if (exception_msg != null && !"".equals(exception_msg)) {
                    exception_msg = filterOffUtf8Mb4(exception_msg).replaceAll("\n", "")
                            .replaceAll("\r", "")
                            .replaceAll("\t", "")
                            .replaceAll("\b", "");
                }
                remark = jsonObject.getString("remark");
                if (remark != null && !"".equals(remark)) {
                    remark = filterOffUtf8Mb4(remark).replaceAll("\n", "")
                            .replaceAll("\r", "")
                            .replaceAll("\t", "")
                            .replaceAll("\b", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }
}
