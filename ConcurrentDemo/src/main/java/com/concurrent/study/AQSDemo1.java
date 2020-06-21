package com.concurrent.study;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/9 21:19
 */
public class AQSDemo1 {

	private static Stack<String> queue = new Stack<>();

	private static ReentrantLock reentrantLock = new ReentrantLock();

	private static Condition producerCondition = reentrantLock.newCondition();

	private static Condition consumerCondition = reentrantLock.newCondition();

	private static final Integer MAX_SIZE = 10;

	private static Integer count = 0;

	private static final Object lock = new Object();

	static class ProducerAndConsume {

		public void messageProducer() throws InterruptedException {
			reentrantLock.lock();
			while (queue.size() >= MAX_SIZE) {
				System.out.println("当前队列消息已满！");
				producerCondition.await();
			}
			queue.push(Thread.currentThread().getName() + System.nanoTime());
			count++;
			consumerCondition.signalAll();
			reentrantLock.unlock();
		}

		public void messageConsumer() throws InterruptedException {
			reentrantLock.lock();
			while (queue.size() == 0) {
				System.out.println("当前队列消息为空！");
				consumerCondition.await();
			}
			System.out.println(queue.pop());
			count--;
			producerCondition.signalAll();
			reentrantLock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 2; i++) {
			new Thread(() -> {

				ProducerAndConsume producerAndConsume = new ProducerAndConsume();
				for (int j = 0; j < 10; j++) {
					try {
						producerAndConsume.messageProducer();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				ProducerAndConsume producerAndConsume = new ProducerAndConsume();
				for (int j = 0; j < 5; j++) {
					try {
						producerAndConsume.messageConsumer();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		System.out.println(count);
		System.out.println(queue.size());
	}
}
