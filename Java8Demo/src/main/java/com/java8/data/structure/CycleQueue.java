package com.java8.data.structure;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-02-04 11:24
 */
public class CycleQueue {

	/**
	 * 基于数组实现循环队列
	 */
	private String[] array;

	/**
	 * 循环队列的容量
	 */
	private int capacity;

	/**
	 * 循环队列的尾部
	 */
	private int tail = 0;

	/**
	 * 循环队列的首部
	 */
	private int head = 0;

	public CycleQueue(int capacity) {
		// 为了解决tail位置空闲的原因
		capacity++;
		array = new String[capacity];
		this.capacity = capacity;
	}

	/**
	 * 入队
	 * @param item 数
	 */
	public boolean enqueue(String item) {
		if ((tail + 1) % capacity == head) {
			return false;
		}
		array[tail] = item;
		tail = (tail + 1) % capacity;
		return true;
	}

	/**
	 * 出队
	 */
	public String dequeue() {
		if (head == tail) {
			return "";
		}
		String result = array[head];
		head = (head + 1) % capacity;
		return result;
	}

	public static void main(String[] args) {
		CycleQueue cycleQueue = new CycleQueue(10);
		for (int i = 0; i < 12; i++) {
			String itemStr = String.format("HelloWorld_%s", i);
			System.out.println(String.format("%s 入队结果为: %s", itemStr, cycleQueue.enqueue(itemStr)));
		}
		for (int i = 0; i < 8; i++) {
			System.out.println(cycleQueue.dequeue());
		}

	}
}
