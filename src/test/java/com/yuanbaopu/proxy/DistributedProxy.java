package com.yuanbaopu.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;

import com.yuanbaopu.ip.IPort;
import com.yuanbaopu.proxy.helper.ZkHelper;

public class DistributedProxy {
	
	@Test
	public void testTaobao() throws IOException {
		IPort iport = getIPort();
		String url = "http://shop72080290.taobao.com/";
		Connection conn = Jsoup.connect(url);
		String html = conn.timeout(21000).proxy(iport.getIp(), 
				iport.getPort(),null).execute().body();
		System.out.println(html);
	}
	
	private IPort getIPort() {
		List<String> datas = ZkHelper.getWorkers();
		int random = randomInt();
		String ip = datas.get(random % datas.size());
		return new IPort(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]));
	}
	
	private static int randomInt() {
		return new Long(Math.round(Math.random() * 10)).intValue();
	}
	
	@Test 
	public void testThread() {
		final String url = "http://shop72080290.taobao.com/";
		final int num = 5;
		List<Thread> ts = new ArrayList<Thread>();
		for(int i = 0; i < 8; i++) {
			final int j = i;
			Thread t = new Thread("" + i) {
				int n = 0;
				public void run() {
					while ((n++) < num) {
						try {
							IPort iport = getIPort();
							Jsoup.connect(url).timeout(21000).proxy(iport.getIp(), iport.getPort(),null).execute().body();
							System.out.println("thread = " + this.getName() + ":" + (j*num + n));
						} catch (IOException e) {
							//e.printStackTrace();
						}
					}
				}
			};
			ts.add(t);
			t.start();
		}
		for(Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("THREAD STOP.");
	}
	
}
