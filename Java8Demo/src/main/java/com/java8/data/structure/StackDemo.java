package com.java8.data.structure;

import java.util.Stack;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-02-04 10:11
 */
public class StackDemo {

	public static void main(String[] args) {
		Stack<Character> stack = new Stack<>();


		while (!stack.empty()){

			System.out.println('c' == '1');
			System.out.println(stack.pop());
		}
	}
}
