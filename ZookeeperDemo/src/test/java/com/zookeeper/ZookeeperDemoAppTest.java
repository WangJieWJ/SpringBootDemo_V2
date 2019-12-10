package com.zookeeper;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

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


    private static final String connectStr = "10.199.98.67:2181";

    /**
     * 超时事件
     */
    private static final int SESSION_TIME_OUT = 6000;

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
//        LOGGER.info("dubbo / => {}", zooKeeper.getChildren("/dubbo", true));
//        LOGGER.info("dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider / => {}", zooKeeper.getChildren("/dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider", true));
//        LOGGER.info("dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider data / => {}", zooKeeper.getChildren("/dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider/configurators", false, null));
//        LOGGER.info("dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider data / => {}", zooKeeper.getChildren("/dubbo/com.haier.other.provider.interfaces.IProductSmsInfoProvider/providers", false, null));
//        LOGGER.info("dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider / => {}", zooKeeper.getChildren("/dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider", true));
//        LOGGER.info("dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider data / => {}", new String(zooKeeper.getData("/dubbo/dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider/configurators", false, null)));
//        LOGGER.info("dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider data / => {}", new String(zooKeeper.getData("/dubbo/dubbo/com.haier.vipUser.provider.interfaces.IAppActionProvider/providers", false, null)));
//        LOGGER.info("dubbo/com.haier.bbs.privider.interfaces.IBbsTreadServiceProvider / => {}", zooKeeper.getChildren("/dubbo/com.haier.bbs.privider.interfaces.IBbsTreadServiceProvider", true));
//        LOGGER.info("services / => {}", zooKeeper.getChildren("/services", true));
//        LOGGER.info("services data / => {}", new String(zooKeeper.getData("/services", false, null)));
//        LOGGER.info("zookeeper / => {}", zooKeeper.getChildren("/zookeeper", true));
        int a = 0;
        while (a < 10) {
            LOGGER.info("zookeeper data / => {}", new String(zooKeeper.getData("/data-center/consumers/log_record_group_kettle/offsets/log_record_topic_prod/0", false, null)));
            Thread.sleep(1000L);
            a++;
        }
//        LOGGER.info("services / => {}", zooKeeper.getChildren("/services", true));
//
//        LOGGER.info("zookeeper / => {}", zooKeeper.getChildren("/zookeeper", true));
//        LOGGER.info("data-center / => {}", zooKeeper.getChildren("/data-center", true));
//        deletePath(zooKeeper, "/data-center");
//
//        LOGGER.info("data-center/cluster/id / => {}", new String(zooKeeper.getData("/data-center/cluster/id", false, null)));
//        LOGGER.info("data-center/brokers / => {}", zooKeeper.getChildren("/data-center/brokers", false));
//        LOGGER.info("data-center/brokers/ids / => {}", zooKeeper.getChildren("/data-center/brokers/ids", false));


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

    public static void deletePath(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        List<String> childList = zooKeeper.getChildren(path, false);
        if (childList.isEmpty()) {
            zooKeeper.delete(path, -1);
            return;
        }
        for (String item : childList) {
            deletePath(zooKeeper, path + "/" + item);
        }
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