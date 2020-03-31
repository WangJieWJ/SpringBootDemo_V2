package com.java8.data.structure;

/**
 * Title: 
 * Description: 排序算法
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-02-04 20:52
 */
public class SortAlgo {

	public static void main(String[] args) {
		int[] array = new int[] { 4, 5, 6, 3, 2, 1 };
		bubbleSort(array);
		insertionSort(array, true);
		insertionSort(array, false);
		for (int item : array) {
			System.out.println(item);
		}
	}

	/**
	 * 冒泡排序
	 * @param array 待排序的数组
	 */
	private static void bubbleSort(int[] array) {
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			// 提前退出冒泡循环的标志位
			boolean jumpFlag = true;
			for (int j = 0; j < arraySize - i - 1; j++) {
				if (array[j] < array[j + 1]) {
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					jumpFlag = false;
				}
			}
			if (jumpFlag) {
				break;
			}
		}
	}

	/**
	 * 插入排序
	 * @param array 待排序的数组
	 * @param asc 是否升序输出
	 */
	private static void insertionSort(int[] array, boolean asc) {
		int arraySize = array.length;
		for (int i = 1; i < arraySize; i++) {
			int sortValue = array[i];
			// 如果想要升序输出
			int j = i - 1;
			for (; j >= 0; j--) {
				if (asc ? sortValue < array[j] : sortValue > array[j]) {
					array[j + 1] = array[j];
				} else {
					break;
				}
			}
			array[j + 1] = sortValue;
		}
	}
}
