package com.yuanbaopu.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.junit.Test;

public class TestPost {

	@Test
	public void doPost() throws IOException {
		String url = "http://www.oschina.net/action/user/hash_login";
		Map<String,String> data = new HashMap<String,String>();
		data.put("email", "langren_qing@163.com");
		data.put("pwd", "eb26f22e2a468bfb5b2e6c7164759931683131f9");
		//data.put("lt", "LT-381893-ecTl9JI0xh4eunqbgP2H70jbadWJt7");
		//data.put("execution", "e1s1");
		data.put("save_login", "1");
		
		Connection conn = Jsoup.connect(url);
		conn.data(data);
		conn.method(Method.POST);
		String html = conn.timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
		
	}
	
	@Test
	public void doDownloan() throws IOException {
		String url = "http://120.25.201.82:8888/root/gongmao/repository/archive.zip?ref=master";
		Map<String,String> data = new HashMap<String,String>();
		data.put("ref", "master");
		
		Connection conn = Jsoup.connect(url);
		conn.data(data);
		conn.cookie("_gitlab_session", "fef026450b76683633f7f6389f5084ba");
		conn.method(Method.GET);
		String html = conn.timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
	}
	
	@Test
	public void downloan() throws IOException {
		String url = "http://120.25.201.82:8888/root/gongmao/repository/archive.zip?ref=master";
		Map<String,String> data = new HashMap<String,String>();
		data.put("ref", "master");
		
		Connection conn = Jsoup.connect(url);
		conn.data(data);
		conn.cookie("_gitlab_session", "fef026450b76683633f7f6389f5084ba");
		conn.method(Method.GET);
		String html = conn.timeout(21000).execute().body();
		System.out.println(html);
	}
	
	//
	@Test
	public void testAlitrip() throws IOException {
		String url = "http://items.alitrip.com/item.htm?spm=a1z10.3-b.w4011-11598148097.118.OIWvTy&id=526573708921&rn=e9b19e560ffa50653e7364fcf25d147b&abbucket=14";
		Connection conn = Jsoup.connect(url);
		String html = conn.timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
	}
	@Test
	public void testTaobao() throws IOException {
		String url = "http://www.taobao.com";
		Connection conn = Jsoup.connect(url);
		String html = conn.timeout(21000).proxy("localhost", 8888,null).execute().body();
		System.out.println(html);
	}
	
	@Test
	public void testHttps() throws IOException {
		String url = "https://siemens-home.tmall.com/";
		Connection conn = Jsoup.connect(url);
		String html = conn.timeout(21000).execute().body();
		System.out.println(html);
	}
	
	@Test
	public void testHttpsWithProxy() {
		try {
			String url = "https://siemens-home.tmall.com/";
			Connection conn = Jsoup.connect(url);
			conn.validateTLSCertificates(false);
			String html = conn.timeout(21000).proxy("localhost", 8888,null).execute().body();
			System.out.println(html);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
