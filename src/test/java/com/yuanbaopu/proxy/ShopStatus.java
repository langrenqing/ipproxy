package com.yuanbaopu.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import com.yuanbaopu.ip.mayi.MayiAuth;
import com.yuanbaopu.proxy.ShopStatus2.Shop;
import com.yuanbaopu.proxy.auth.AuthProvider;
import com.yuanbaopu.util.HttpsUtil;

public class ShopStatus {

	public static void main(String[] args) throws IOException, InterruptedException {
		//List<Shop> shops = FileUtils.read("D:\\datag\\shop_list.txt");
		
		List<Shop> shops = new ArrayList<Shop>();
		shops.add(new Shop("aa3450edbf6a42e2a082e0217606d88f","http://xiaowenmeipu.taobao.com/"));
		int i = 0, f = 0;
		for(Shop shop : shops) {
//			if(shop.getUrl().indexOf("tmall") == -1) {
//				continue;
//			}
			String status = getShopStatus(visit(shop.getUrl()));
			System.out.println(i + " ,url =" + shop.getUrl() + ",uuID=" + shop.getShopId()+ ",status=" + status);
			i++;
			//if(i > 20) {
			//	break;
			//}
			if("WARN".equals(status)) {
				f++;
			}
			System.out.println("采集统计,总次数：" + i + "，失败：" + f);
			Thread.sleep(Math.round(Math.random()*1000));
		}
	}
	
	public static String getShopStatus(String body) {
	    String status = "";
	    System.out.println(body);
	    if (body == null) {
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
			HttpsUtil.trustEveryone();
			//"182.92.1.222", 8123
			//.proxy("182.92.1.222", 8123)
			//.proxy("localhost", 8888,null)
	 		//return Jsoup.connect(url).proxy("182.92.1.222", 8123,HTTPCommonUtil.getAuthHeader()).get().html();
			AuthProvider authp = new MayiAuth(); 
	 		return Jsoup.connect(url).proxy("182.92.1.222", 8123, authp.getAuth()).execute().body();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

