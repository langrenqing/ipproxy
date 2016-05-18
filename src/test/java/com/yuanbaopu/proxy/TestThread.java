package com.yuanbaopu.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.junit.Test;

public class TestThread {

	@Test 
	public void testThread() {
		final String url = "http://shop72080290.taobao.com/";
		final int num = 10;
		List<Thread> ts = new ArrayList<Thread>();
		for(int i = 0; i < 8; i++) {
			final int j = i;
			Thread t = new Thread("" + i) {
				int n = 0;
				public void run() {
					while ((n++) < num) {
						try {
							Jsoup.connect(url).timeout(21000).proxy("localhost", 8888,null).execute().body();
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
