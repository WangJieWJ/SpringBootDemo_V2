package com.concurrent.study;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/6/22 21:36
 */
public class ForkJoinPoolDemo {

	private static final Integer MIN_TASK = 50;

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		SumTask sumTask = new SumTask(1, 101);
		Future<Integer> sumResult = forkJoinPool.submit(sumTask);
		System.out.println(sumResult.get());
	}

	static class SumTask extends RecursiveTask<Integer> {

		private int startNum;

		private int endNum;

		SumTask(int startNum, int endNum) {
			this.startNum = startNum;
			this.endNum = endNum;
		}

		@Override
		protected Integer compute() {
			if ((endNum - startNum) <= MIN_TASK) {
				int sum = 0;
				for (int i = startNum; i < endNum; i++) {
					sum += i;
				}
				return sum;
			}
			int temp = startNum + (endNum - startNum) / 2;
			SumTask firstSumTask = new SumTask(startNum, temp);
			SumTask secondSumTask = new SumTask(temp, endNum);
			firstSumTask.fork();
			secondSumTask.fork();
			return firstSumTask.join() + secondSumTask.join();
		}
	}
}
