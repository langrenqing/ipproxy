package com.yuanbaopu.ip.mayi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.base.Joiner;
import com.yuanbaopu.proxy.auth.AuthProvider;
import com.yuanbaopu.proxy.helper.ProxyHelper;

public class MayiAuth implements AuthProvider {
	
	Map<String, String> authInfos = new HashMap<String,String>();
	
	public MayiAuth() {
		authInfos.put("appkey", ProxyHelper.getString("mayi.appkey"));
		authInfos.put("secret", ProxyHelper.getString("mayi.secret"));
	}

	@Override
	public String getAuth() {
		
		String appkey = authInfos.get("appkey");
 		String secret = authInfos.get("secret");
 		 
 		// 创建参数表
 		Map<String,String> paramMap = new HashMap<String,String>();
 		paramMap.put("app_key", appkey);
 		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));//使用中国时间，以免时区不同导致认证错误
 		paramMap.put("timestamp", format.format(new Date()));
 		 
 		// 对参数名进行排序
 		String[] keyArray = (String[]) paramMap.keySet().toArray(new String[0]);
 		Arrays.sort(keyArray);
 		
 		// 拼接有序的参数名-值串
 		StringBuilder stringBuilder = new StringBuilder();
 		stringBuilder.append(secret);
 		for(String key : keyArray){
 		    stringBuilder.append(key).append(paramMap.get(key));
 		}
 		     
 		stringBuilder.append(secret);
 		String codes = stringBuilder.toString();
 		//System.out.println(codes);
 		// MD5编码并转为大写， 这里使用的是Apache codec
 		String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(codes).toUpperCase();

 		paramMap.put("sign", sign);

 		// 拼装请求头Proxy-Authorization的值，这里使用 guava 进行map的拼接
 		String authHeader = "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);
 		
 		return authHeader;
	}

}
