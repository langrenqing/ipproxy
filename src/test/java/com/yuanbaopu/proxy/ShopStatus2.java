package com.yuanbaopu.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

public class ShopStatus2 {
	
	static public class Shop {
		String shopId;
		String url;
		
		public Shop(String shopId, String url) {
			this.shopId = shopId;
			this.url = url;
		}
		
		public String getShopId() {
			return shopId;
		}
		public void setShopId(String shopId) {
			this.shopId = shopId;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return "Shop [shopId=" + shopId + ", url=" + url + "]";
		}
		
	}

	public static void start(String[] args) throws IOException, InterruptedException {
		//List<Shop> shops = FileUtils.read("D:\\datag\\shop_list.txt");
		List<Shop> shops = new ArrayList<Shop>();
		shops.add(new Shop("aa3450edbf6a42e2a082e0217606d88f","http://img.alicdn.com/imgextra/i1/1714128138/TB289V7hFXXXXbGXpXXXXXXXXXX-1714128138.jpg"));
		int i = 0, f = 0;
		for(Shop shop : shops) {
//			if(shop.getUrl().indexOf("tmall") == -1) {
//				continue;
//			}
			String status = getShopStatus(visit(shop.getUrl()));
			System.out.println(i + " ,url =" + shop.getUrl() + ",uuID=" + shop.getShopId()+ ",status=" + status);
			i++;
			if("WARN".equals(status)) {
				f++;
			}
			System.out.println("采集统计,总次数：" + i + "，失败：" + f);
			Thread.sleep(Math.round(Math.random()*1000));
		}
	}
	
	public static String getShopStatus(String body) {
	    String status = "";
	    if (body == null) {
	    	System.out.println(body);
	        status = "WARN";
	    } else if (body.toString().indexOf("没有找到相应的店铺信息") > -1 
	    		|| body.toString().indexOf("您访问的店铺不存在") >= 0) {
	        status = "CLOSED";
	    } else {
	        status = "NOMAL";
	    }
	    return status;
	}
	
	public static String visit(String url) {
		try {
			System.out.println("url = " + url);
			return Jsoup.connect(url).timeout(11000).proxy("localhost", 8888,null).get().html();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

