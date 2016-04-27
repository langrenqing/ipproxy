package com.yuanbaopu.proxy.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;

public class ZkHelper {
	
	private static CuratorFramework client = newClient();
	private final static String NAMESPACE = "/ipproxy";
	private final static String WORKER_NAMESPACE = "/ipproxy/workers";
	
	public static String getNamespace() {
		return NAMESPACE;
	}
	
	public static CuratorFramework newClient() {
		if(client == null) {
			client = CuratorFrameworkFactory.newClient(ProxyHelper.getString("proxy.zk.string"), 
				new ExponentialBackoffRetry(1000, Integer.MAX_VALUE));
			client.start();
			try {
				client.createContainers(WORKER_NAMESPACE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			shutdown();
		}
		return client;
	}
	
	public static void asWorker(String iPort) {
		try {
			newClient().create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(ZKPaths.makePath(WORKER_NAMESPACE, "a"), iPort.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getWorkers() {
		List<String> dataList = new ArrayList<String>(); 
		PathChildrenCache cache = new PathChildrenCache(newClient(), WORKER_NAMESPACE, true);
	    try {
			cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
			List<ChildData> datas = cache.getCurrentData();
			for (ChildData data : datas) {
				dataList.add(new String(data.getData()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseableUtils.closeQuietly(cache);
		}
		return dataList;
	}
	
	private static void shutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("zk client is shutdown");
				ZkHelper.newClient().close();
			}
			
		});
	}
	
	public static void main(String[] args) {
		ZkHelper.newClient();
		ZkHelper.asWorker("192.168.5.234:8888");
		ZkHelper.asWorker("192.168.4.164:8888");
		List<String> datas = ZkHelper.getWorkers();
		for(String data : datas) {
			System.out.println(data);
		}
	}
	
}
