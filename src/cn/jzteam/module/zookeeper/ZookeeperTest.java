package cn.jzteam.module.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

public class ZookeeperTest implements Watcher {

	// 多线程协调计数器
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws IOException {
		// 链接地址
		String connectStr = "127.0.0.1:2181";
		// 超时时间
		int sessionTimeout = 50000;
		// 事件通知器
		Watcher watcher = new ZookeeperTest();
		// 创建会话实例
		ZooKeeper zookeeper = new ZooKeeper(connectStr, sessionTimeout, watcher);
		System.out.println("zookeeper状态111：" + zookeeper.getState());

		try {
			connectedSemaphore.await();
		} catch (InterruptedException e) {
		}
		System.out.println("zookeeper实例建立完成：state=" + zookeeper.getState());

		System.out.println("会话创建完毕11===================================");
		
		ZookeeperTest instance = new ZookeeperTest();
		
		try {
			// 查询节点内容
			byte[] data = instance.getData(zookeeper);
			System.out.println("使用另一个会话查询到节点内容："+new String(data));
		} catch (Exception e) {
			System.out.println("查询节点内容异常"+e.getMessage());
		}
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
	
	@Override
	public void process(WatchedEvent event) {
		System.out.println("接受到watch事件："+event);
		if(KeeperState.SyncConnected == event.getState()){
			// 计数器-1，计算器达到0时，不再阻塞
			connectedSemaphore.countDown();
		}
	}
}
