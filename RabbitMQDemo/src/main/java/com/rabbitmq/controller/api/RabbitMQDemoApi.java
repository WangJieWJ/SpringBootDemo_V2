package com.rabbitmq.controller.api;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2018/12/16 16:06
 */
@Api(tags = "RabbitMQDemo", description = "RabbitMQ测试用例")
public interface RabbitMQDemoApi {

    @ApiOperation(value = "发送topic消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "发送的消息", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "uniqueId", value = "唯一标示", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(name = "exchangeName", value = "交换器唯一表示", dataTypeClass = String.class, required = false),
            @ApiImplicitParam(name = "routingKey", value = "routingKey", dataTypeClass = String.class, required = false),
    })
    public JSONObject sendTopicMsg(String msg, String uniqueId, String exchangeName, String routingKey);
}
