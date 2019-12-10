package com.kafka.controller.api;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/9 18:06
 */
@Api(value = "kafkaDemo", description = "Kafka发送测试")
public interface KafkaDemoApi {

    @ApiOperation(value = "发送信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicName", value = "topic名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "partition", value = "要发往哪个分区", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "key", value = "消息的键", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "msg", value = "消息的值", required = true, dataTypeClass = String.class)
    })
    JSONObject producerMsg(String topicName, Integer partition, String key, String msg);


    @ApiOperation(value = "批量发送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgNum", value = "批量发送消息的数量", required = true, dataTypeClass = Integer.class),
    })
    JSONObject producerBatchMsg(Integer msgNum);
}
