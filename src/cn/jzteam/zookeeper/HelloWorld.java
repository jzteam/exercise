package cn.jzteam.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class HelloWorld implements Watcher {
    // 多线程协调计数器
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        // 链接地址
        String connectStr = "127.0.0.1:2181";
        // 超时时间
        int sessionTimeout = 10000;
        // 事件通知器
        Watcher watcher = new HelloWorld();
        // 创建会话实例
        ZooKeeper zookeeper = new ZooKeeper(connectStr, sessionTimeout, watcher);
        System.out.println("zookeeper状态：" + zookeeper.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
        }
        System.out.println("zookeeper实例建立完成：state=" + zookeeper.getState());
        zookeeper.addAuthInfo("digest", "foo:false".getBytes());
        System.out.println("设置权限！");
        /*
         * long sessionId = zookeeper.getSessionId(); byte[] sessionPasswd =
         * zookeeper.getSessionPasswd();
         * 
         * ZooKeeper zookeeper2 = new
         * ZooKeeper(connectStr,sessionTimeout,watcher
         * ,sessionId,sessionPasswd,true); try { connectedSemaphore.await(); }
         * catch (InterruptedException e) { }
         * System.out.println("zookeeper2实例建立完成：state="+zookeeper.getState());
         */

        System.out.println("会话创建完毕===================================");

        HelloWorld instance = new HelloWorld();
        try {
            // 增加节点
            Stat exists = zookeeper.exists("/zk", null);
            if (exists != null) {
                System.out.println("节点已存在，version=" + exists.getVersion());
                zookeeper.delete("/zk", exists.getVersion());
                System.out.println("删除节点！");
            }
            instance.create(zookeeper);
        } catch (Exception e) {
            System.out.println("新增节点异常" + e.getMessage());
        }


        try {
            // 查询节点
            List<String> children = instance.getChildren(zookeeper);
            System.out.println("查询到节点" + children);
        } catch (Exception e) {
            System.out.println("查询节点异常" + e.getMessage());
        }

        try {
            // 查询节点内容
            byte[] data = instance.getData(zookeeper);
            System.out.println("查询到节点内容：" + new String(data));
        } catch (Exception e) {
            System.out.println("查询节点内容异常" + e.getMessage());
        }

        try {
            // 更新节点内容
            Stat stat = instance.setData(zookeeper);
            System.out.println("更新节点内容：当前version=" + stat.getVersion());
        } catch (Exception e) {
            System.out.println("更新节点内容异常：" + e.getMessage());
        }

        /*
         * try { zookeeper.delete("/zk", -1); System.out.println("删除节点"); }
         * catch (Exception e) { System.out.println("删除节点异常："+e.getMessage()); }
         */

    }

    // 更新节点内容
    private Stat setData(ZooKeeper zk) throws KeeperException, InterruptedException {
        String path = "/zk";
        Stat stat = zk.setData(path, "jzteam测试修改节点内容".getBytes(), -1);
        System.out.println("第一次修改后：version=" + stat.getVersion());
        Stat stat1 = zk.setData(path, "jzteam测试修改节点内容".getBytes(), stat.getVersion());
        System.out.println("第二次修改后：version=" + stat1.getVersion());
        try {
            zk.setData(path, "jzteam测试修改节点内容".getBytes(), stat.getVersion());
        } catch (Exception e) {
            System.out.println("第三次修改，使用version=" + stat.getVersion() + ",异常：" + e.getMessage());
        }
        return stat1;
    }

    // 查询节点内容
    private byte[] getData(ZooKeeper zk) throws KeeperException, InterruptedException {
        String path = "/zk";
        byte[] data = zk.getData(path, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == EventType.NodeDataChanged) {
                    System.out.println(event.getPath() + "节点内容被修改了！");
                }
            }

        }, new Stat());
        return data;
    }

    // 获取节点
    private List<String> getChildren(ZooKeeper zk) throws KeeperException, InterruptedException {
        String path = "/";
        List<String> children = zk.getChildren(path, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == EventType.NodeDataChanged) {
                    System.out.println(event.getPath() + "节点被修改了！");
                }
            }

        });
        return children;
    }

    // 新增节点
    private void create(ZooKeeper zk) throws KeeperException, InterruptedException {
        String path = "/zk";
        byte[] data = "一致性zookeeper".getBytes();
        ArrayList<ACL> acl = Ids.OPEN_ACL_UNSAFE;
        CreateMode createMode = CreateMode.EPHEMERAL;

        zk.create(path, data, acl, createMode);

        System.out.println("创建znode完成：" + path);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("接受到watch事件：" + event);
        if (KeeperState.SyncConnected == event.getState()) {
            // 计数器-1，计算器达到0时，不再阻塞
            connectedSemaphore.countDown();
        }
    }

}
