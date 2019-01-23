package com.druid.controller;

import com.druid.controller.api.DruidApi;
import com.druid.dto.UserAddDTO;
import com.druid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 11:04
 */
@RestController
@RequestMapping(value = "/druid")
public class DruidController implements DruidApi {

    @Autowired
    private UserService userService;

    @Override
    @PostMapping(value = "/saveUserInfo")
    public void saveUserInfo(@RequestBody UserAddDTO userAddDTO) {
        userService.saveUserInfo(userAddDTO);
    }
}
