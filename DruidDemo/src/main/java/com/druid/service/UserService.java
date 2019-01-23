package com.druid.service;

import com.druid.dao.UserDao;
import com.druid.dto.UserAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 17:59
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void saveUserInfo(UserAddDTO userAddDTO) {
        userDao.saveUserInfo(userAddDTO);
    }
}
