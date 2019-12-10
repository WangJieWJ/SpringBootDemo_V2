package com.java8.service;

import java.util.Date;
import java.util.Random;

/**
 * Title:
 * Description: 等待唤醒机制的规范实现
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-27 18:59
 */
public class GuardedSuspensionService {

    public class RpcMessage {
        /**
         * 消息唯一ID
         */
        private Long msgId;
        /**
         * 消息发送时间
         */
        private Date sendTime;
        /**
         * 消息响应时间
         */
        private Date responseTime;
        /**
         * 响应数据
         */
        private Object data;

        public Long getMsgId() {
            return msgId;
        }

        public void setMsgId(Long msgId) {
            this.msgId = msgId;
        }

        public Date getSendTime() {
            return sendTime;
        }

        public void setSendTime(Date sendTime) {
            this.sendTime = sendTime;
        }

        public Date getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(Date responseTime) {
            this.responseTime = responseTime;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    /**
     * @param data RPC数据
     */
    public void handleWebReq(String data) {
        // 生成一个唯一ID
        Long msgId = (new Random()).nextLong();
        // 创建一条消息
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMsgId(msgId);
        rpcMessage.setSendTime(new Date());
        rpcMessage.setData(data);
        // 创建GuardedObject实例
        GuardedObject<RpcMessage> go = GuardedObject.create(msgId);
        // 发送消息逻辑

        // 等待MQ消息
        RpcMessage r = go.get(t -> t != null);
        // 返回接收到的消息
    }
}
