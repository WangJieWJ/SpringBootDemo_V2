package com.druid.controller.api;

import com.druid.dto.UserAddDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 11:05
 */
@Api(value = "Druid", description = "Druid测试")
public interface DruidApi {


	@ApiOperation(value = "新增用户")
	@ApiImplicitParam(name = "userAddDTO", value = "用户信息", dataType = "UserAddDTO", required = true)
	void saveUserInfo(@RequestBody UserAddDTO userAddDTO);

	@ApiOperation(value = "事务管理")
	void transaction();
}
