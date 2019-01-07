package com.zookeeper.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/7 20:30
 */
@Api(tags = "ZookeeperDemo", description = "Zookeeper测试用例")
public interface ZookeeperDemoApi {

    @ApiOperation(value = "获取Zookeeper中的数据")
    void getzkData();
}
