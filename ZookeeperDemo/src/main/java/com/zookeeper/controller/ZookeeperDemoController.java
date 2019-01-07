package com.zookeeper.controller;

import com.zookeeper.controller.api.ZookeeperDemoApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/7 20:29
 */
@RestController(value = "/zk")
public class ZookeeperDemoController implements ZookeeperDemoApi {

    @Override
    @PostMapping(value = "/get")
    public void getzkData() {

    }
}
