package com.kafka.event;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/8/1 16:39
 */
public class AppEvent implements Serializable {

    private Long eventId;
    private String eventType;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    @Override
    public String toString() {
        return String.format("eventName:%s,eventData:%s", this.getClass().getName(), JSON.toJSONString(this));
    }
}
