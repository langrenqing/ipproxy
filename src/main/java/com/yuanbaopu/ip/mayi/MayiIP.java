package com.yuanbaopu.ip.mayi;

import com.yuanbaopu.ip.DefaultIPool;
import com.yuanbaopu.ip.IPort;
import com.yuanbaopu.proxy.auth.AuthProvider;
import com.yuanbaopu.proxy.helper.ProxyHelper;

public class MayiIP extends DefaultIPool {
	
	AuthProvider authp = new MayiAuth(); 
	
	public MayiIP() {
		addIP(new IPort(ProxyHelper.getString("mayi.ip"), 
				ProxyHelper.getInt("mayi.port"), authp));
	}

}
