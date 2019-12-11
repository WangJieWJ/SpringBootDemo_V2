package com.shiro.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-12-10 16:52
 */
public class HashUtils {

	public static final String ALGORITHM_NAME = "md5";

	public static final int HASH_ITERATIONS = 2;

	public static String getEncryptPassWord(String password, String salt) {
		return new SimpleHash(ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
	}
}
