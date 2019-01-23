package com.druid.controller.api;

import com.druid.dto.UserAddDTO;
import com.druid.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

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
    @ApiImplicitParam(name = "userAddDTO", value = "用户信息", dataTypeClass = UserAddDTO.class, required = true)
    void saveUserInfo(UserAddDTO userAddDTO);

}
