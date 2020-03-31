package com.java8.data.structure;


import java.util.Objects;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-02-03 21:06
 */
public class LinkedNode {


	/**
	 * 当前节点数据
	 */
	private String nodeData;

	/**
	 * 下一个节点
	 */
	private LinkedNode nextNode;

	public LinkedNode(String nodeData) {
		this.nodeData = nodeData;
	}

	public LinkedNode(String nodeData, LinkedNode nextNode) {
		this.nodeData = nodeData;
		this.nextNode = nextNode;
	}

	public String getNodeData() {
		return nodeData;
	}

	public void setNodeData(String nodeData) {
		this.nodeData = nodeData;
	}


	public LinkedNode getNextNode() {
		return nextNode;
	}

	public void setNextNode(LinkedNode nextNode) {
		this.nextNode = nextNode;
	}

	@Override
	public String toString() {
		return "LinkedNode{" +
				"nodeData='" + nodeData + '\'' +
				'}';
	}

	public static void main(String[] args) {
		LinkedNode linkedNode1 = new LinkedNode("firste");
		LinkedNode linkedNode2 = new LinkedNode("secondN");
		LinkedNode linkedNode3 = new LinkedNode("thirdNo22");
		LinkedNode linkedNode4 = new LinkedNode("fourNod333e");
		linkedNode1.setNextNode(linkedNode2);
		linkedNode2.setNextNode(linkedNode3);
		linkedNode3.setNextNode(linkedNode4);
		linkedNode4.setNextNode(linkedNode2);

		System.out.println(linkedNode1);

//		System.out.println(reverse(linkedNode1));

//		System.out.println(detectionLoop(linkedNode1));
	}

	/**
	 * 单链表 反转
	 * @param currentNode 链表首节点
	 * @return 反转之后的首节点
	 */
	private static LinkedNode reverse(LinkedNode currentNode) {
		if (Objects.isNull(currentNode) || Objects.isNull(currentNode.getNextNode())) {
			return currentNode;
		}
		LinkedNode reLinkedNode = reverse(currentNode.getNextNode());
		currentNode.getNextNode().setNextNode(currentNode);
		currentNode.setNextNode(null);
		return reLinkedNode;
	}

	/**
	 * 判断一个链表是否有环
	 * @param currentNode 当前链表的首节点
	 * @return 是否有环
	 */
	private static boolean detectionLoop(LinkedNode currentNode) {
		if (currentNode == null) {
			return false;
		}
		LinkedNode slowNode = currentNode;
		LinkedNode fastNode = currentNode;
		boolean detectionLoop = false;
		while (true) {
			System.out.println(slowNode.getNextNode());
			slowNode = slowNode.getNextNode();
			if (slowNode == null) {
				break;
			}
			fastNode = fastNode.getNextNode().getNextNode();
			if (fastNode == null || fastNode.getNextNode() == null) {
				break;
			}
			if (fastNode == slowNode) {
				detectionLoop = true;
				break;
			}
		}
		return detectionLoop;
	}

//	private static LinkedNode mergeLinkedNode(LinkedNode firstLinkedNode, LinkedNode secondLinkedNode) {
//		if (firstLinkedNode == null) {
//			return secondLinkedNode;
//		}
//		if (secondLinkedNode == null) {
//			return firstLinkedNode;
//		}
//		LinkedNode linkedNode = new LinkedNode("-1");
//		LinkedNode currentNode = linkedNode;
//		linkedNode.setNextNode(currentNode);
//		while (firstLinkedNode != null && secondLinkedNode != null) {
//
//			if(firstLinkedNode.getNodeData().length() < secondLinkedNode.getNodeData().length()){
//				linkedNode.setNextNode(firstLinkedNode);
//				firstLinkedNode = firstLinkedNode.getNextNode();
//			}else {
//				linkedNode.setNextNode(secondLinkedNode);
//				secondLinkedNode = secondLinkedNode.getNextNode();
//			}
//		}
//	}

}
