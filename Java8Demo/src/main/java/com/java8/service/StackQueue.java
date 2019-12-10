package com.java8.service;

import java.util.Stack;

/**
 * Title: 通过Stack实现Queue
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-15 21:31
 */
public class StackQueue {

    private Stack<Integer> stackA = new Stack();
    private Stack<Integer> stackB = new Stack();

    /**
     * 入队
     *
     * @param element
     */
    public void enQueue(int element) {
        stackA.push(element);
    }

    /**
     * 删除
     *
     * @return
     */
    public Integer deQueue() {
        if (stackB.isEmpty()) {
            if (stackA.isEmpty()) {
                return null;
            }
            transfer();
        }
        return stackB.pop();
    }

    /**
     * 将A中的元素转移到B中
     */
    private void transfer() {
        while (!stackA.isEmpty()) {
            stackB.push(stackA.pop());
        }
    }


    public static void main(String[] args) {
        StackQueue stackQueue = new StackQueue();
        stackQueue.enQueue(1);
        stackQueue.enQueue(2);
        stackQueue.enQueue(3);
        System.out.println(stackQueue.deQueue());
        System.out.println(stackQueue.deQueue());
        stackQueue.enQueue(4);
        System.out.println(stackQueue.deQueue());
        System.out.println(stackQueue.deQueue());
    }
}
