package com.druid.dao;

import com.druid.dto.UserAddDTO;
import com.druid.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/22 18:00
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUserInfo(UserAddDTO userAddDTO) {
        StringBuilder insertSQLBuilder = new StringBuilder();
        insertSQLBuilder.append("INSERT INTO `").append(User.TABLE_NAME)
                .append("`(`userName`,`age`,`phone`,`address`) VALUES('")
                .append(userAddDTO.getUserName()).append("','")
                .append(userAddDTO.getAge()).append("','")
                .append(userAddDTO.getPhone()).append("','")
                .append(userAddDTO.getAddress()).append("')");
        jdbcTemplate.execute(insertSQLBuilder.toString());
    }
}
