package com.yuanbaopu.proxy;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;

import com.yuanbaopu.ProxyMain;

public class TestMainProxy {
	
	//@Before
	public void startProxy() throws Exception {
		ProxyMain.appStart();
	}
	
	//@Test
	public void testShopStatus() throws IOException, InterruptedException {
		ShopStatus2.start(null);
		Assert.assertTrue(true);
	}
	
	@Test 
	public void testImage() throws IOException, InterruptedException {
		String url = "http://shop72080290.taobao.com/";
		String html = Jsoup.connect(url).timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
	}
	
	@Test
	public void testHttps() throws IOException {
		String url = "http://www.yuanbaopu.com?aaa=txt";
		String html = Jsoup.connect(url).timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
	}
	
}
