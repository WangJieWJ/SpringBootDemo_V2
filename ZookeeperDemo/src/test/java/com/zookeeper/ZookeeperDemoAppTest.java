package com.zookeeper;


import org.apache.zookeeper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019/1/7 20:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperDemoAppTest {


    private static final String connectStr = "127.0.0.1:2181";

    /**
     * 超时事件
     */
    private static final int SESSION_TIME_OUT = 60000;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDemoAppTest.class);

    @Test
    public void zkDemo() throws IOException, KeeperException, InterruptedException {

        // 创建一个与服务器的连接
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, SESSION_TIME_OUT, new Watcher() {

            // 监听所有被触发的事件
            @Override
            public void process(WatchedEvent event) {
                LOGGER.info("监听事件，EVENT,path:{},state:{},type:{},wrapper:{}", event.getPath(), event.getState(), event.getType(), event.getWrapper());
            }
        });

        // 查看根节点
        LOGGER.info("ls / => {}", zooKeeper.getChildren("/", true));
        LOGGER.info("brokers / => {}", zooKeeper.getChildren("/brokers", true));
        LOGGER.info("consumers / => {}", zooKeeper.getChildren("/consumers", true));
        LOGGER.info("config / => {}", zooKeeper.getChildren("/config", true));
        LOGGER.info("consumers/grouptest / => {}", zooKeeper.getChildren("/consumers/grouptest", true));
        LOGGER.info("consumers/grouptest/offsets / => {}", zooKeeper.getChildren("/consumers/grouptest/offsets", true));
        LOGGER.info("consumers/grouptest/offsets/test / => {}", zooKeeper.getChildren("/consumers/grouptest/offsets/test", true));
        LOGGER.info("consumers/grouptest/offsets/test/0 / => {}", zooKeeper.getChildren("/consumers/grouptest/offsets/test/0", true));
        LOGGER.info("get /consumers/grouptest/offsets/test/0 => {}", new String(zooKeeper.getData("/consumers/grouptest/offsets/test/0", false, null), "utf-8"));


//        // 创建一个目录节点
//        if (zooKeeper.exists("/node", true) == null) {
//            zooKeeper.create("/node", "conan".getBytes("utf-8"),
//                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            LOGGER.info("create /node conan");
//            // 查看/node节点的数据
//            LOGGER.info("get /node:{}", new String(zooKeeper.getData("/node", false, null), "utf-8"));
//            //查看根节点
//            LOGGER.info("ls / => {}", zooKeeper.getChildren("/", true));
//        }
//
//        // 创建一个子目录节点
//        if (zooKeeper.exists("/node/sub1", true) == null) {
//            zooKeeper.create("/node/sub1", "sub1".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            LOGGER.info("create /node/sub1 sub1");
//            // 查看node节点
//            LOGGER.info("ls /node => ", zooKeeper.getChildren("/node", true));
//        }
//
//        // 修改节点数据
//        if (zooKeeper.exists("/node", true) != null) {
//            zooKeeper.setData("/node", "changed".getBytes(), -1);
//            // 查看/node节点数据
//            LOGGER.info("get /node => {}", new String(zooKeeper.getData("/node", false, null), "utf-8"));
//        }
//
//        // 删除节点
//        if (zooKeeper.exists("/node/sub1", true) != null) {
//            zooKeeper.delete("/node/sub1", -1);
//            zooKeeper.delete("/node", -1);
//            // 查看根节点
//            LOGGER.info("ls / => ", zooKeeper.getChildren("/", true));
//        }

        // 关闭连接
        zooKeeper.close();
    }


    @Test
    public void testRemoteDemo() throws Exception {
        // 创建一个与服务器的连接
        ZooKeeper zooKeeper = new ZooKeeper("10.159.62.35:2181", SESSION_TIME_OUT, new Watcher() {

            // 监听所有被触发的事件
            @Override
            public void process(WatchedEvent event) {
                LOGGER.info("监听事件，EVENT,path:{},state:{},type:{},wrapper:{}", event.getPath(), event.getState(), event.getType(), event.getWrapper());
            }
        });

        LOGGER.info("ls / => {}", zooKeeper.getChildren("/", true));
    }

}