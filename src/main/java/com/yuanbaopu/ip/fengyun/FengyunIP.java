package com.yuanbaopu.ip.fengyun;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuanbaopu.ip.DefaultIPool;
import com.yuanbaopu.ip.IPort;

public class FengyunIP extends DefaultIPool {
	
	//禁止ip
//	protected Set<IPort> fiports = new LinkedHashSet<IPort>();
	//使用中
	static final Logger  logger      = LoggerFactory.getLogger(FengyunIP.class);
	protected Set<IPort> uiports     = new HashSet<IPort>();
	IPort 				 blankIP     = new IPort("192.168.1.1",-1);
	FyIProvider 		 provider;
	
	public FengyunIP() {
		provider = new FyIProvider();
	}

	//一个ip的使用次数？ 最大使用（被动），最小使用（主动），循环使用，或者固定次数
	@Override
	public IPort getAIP() {
		//加入到使用中， 从可用中移除
		try {
			IPort iport = provider.take();
			if(!uiports.contains(iport)) {
				uiports.add(iport);
			}
			return iport;
		} catch (InterruptedException e) {
		}
		return blankIP;
	}
	
	//调用方每次请求获取一个ip, 并检测状态为200 以外时切换一个ip; 
	//请求发起方也可以通过在url中附加一个参数而要求切换ip
	
	// 实现为生产者与消费者
	
}
