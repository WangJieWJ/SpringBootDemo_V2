package com.kafka.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Title:
 * Description: 消息序列化中间类
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/10 17:44
 */
public class MessageData implements Serializable {

    /**
     * 消息主键
     */
    private String id;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 发送时间
     */
    private Date sendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
