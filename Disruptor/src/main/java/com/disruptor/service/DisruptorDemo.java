package com.disruptor.service;

import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/24 17:24
 */
public class DisruptorDemo {

	static class OrderEvent {
		/**
		 * 事件触发时间
		 */
		private long eventTime;

		/**
		 * 事件内容
		 */
		private String eventContent;

		public long getEventTime() {
			return eventTime;
		}

		public void setEventTime(long eventTime) {
			this.eventTime = eventTime;
		}

		public String getEventContent() {
			return eventContent;
		}

		public void setEventContent(String eventContent) {
			this.eventContent = eventContent;
		}

		@Override
		public String toString() {
			return "OrderEvent{" +
					"eventTime=" + eventTime +
					", eventContent='" + eventContent + '\'' +
					'}';
		}
	}

	static class OrderFactory implements EventFactory<OrderEvent> {

		@Override
		public OrderEvent newInstance() {
			return new OrderEvent();
		}
	}

	static class OrderHandler implements EventHandler<OrderEvent> {

		@Override
		public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
			System.out.printf("当前线程为:%s,当前遍历的环Id为:%s,正在消费的订单为:%s\n", Thread.currentThread().getName(), sequence, event);
			event.setEventContent(Thread.currentThread().getName());
		}
	}

	public static void testNormal() {
		// 需要为2的倍数
		int ringBufferSize = 16;
		Disruptor<OrderEvent> orderEventDisruptor = new Disruptor<>(new OrderFactory(), ringBufferSize, Executors.defaultThreadFactory());
		orderEventDisruptor.handleEventsWith(new OrderHandler()).then(new OrderHandler(), new OrderHandler());
		orderEventDisruptor.start();
		RingBuffer ringBuffer = orderEventDisruptor.getRingBuffer();
		for (int i = 0; i < 100; i++) {
			long nextIndex = ringBuffer.next();
			OrderEvent orderEvent = (OrderEvent) ringBuffer.get(nextIndex);
			orderEvent.setEventTime(System.nanoTime());
			orderEvent.setEventContent("订单发生时间为:");
			ringBuffer.publish(i);
		}
	}

	public static void testLambdas(){
		int ringBufferSize = 1024;
		Disruptor<OrderEvent> orderDisruptor = new Disruptor<>(OrderEvent::new,ringBufferSize, DaemonThreadFactory.INSTANCE);
		orderDisruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.printf("当前线程为:%s,当前遍历的环Id为:%s,正在消费的订单为:%s%n", Thread.currentThread().getName(), sequence, event));
		orderDisruptor.start();
	}
}
