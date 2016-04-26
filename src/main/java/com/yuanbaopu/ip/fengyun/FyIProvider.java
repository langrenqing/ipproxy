package com.yuanbaopu.ip.fengyun;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuanbaopu.ip.IPort;
import com.yuanbaopu.proxy.helper.ProxyHelper;

public class FyIProvider {
	
	static final Logger        logger  = LoggerFactory.getLogger(FengyunIP.class);
	LinkedBlockingQueue<IPort> ipQueue = new LinkedBlockingQueue<IPort>();
	ScheduledExecutorService   fetchES = Executors.newScheduledThreadPool(1);
	ExecutorService 		   es      = null;
	//禁止ip
	Set<IPort> 				   fiports = new LinkedHashSet<IPort>();
	
	public FyIProvider() {
		int time    = ProxyHelper.getPropertyInt("fengyun.fetch.schdule");
		fetchES.scheduleAtFixedRate(new FetchHandler(), 2, time, TimeUnit.SECONDS);
		int threads = ProxyHelper.getPropertyInt("fengyun.check.threads");
		es          = Executors.newFixedThreadPool(threads);
	}
	
	public IPort take() throws InterruptedException {
		return ipQueue.peek();
	}
	
	class FetchHandler implements Runnable {

		@Override
		public void run() {
			logger.debug("fetch ip");
			fetchIPs();
		}
		//调用第三方接口获取ip list
		public void fetchIPs() {
			// add to 可用
			List<IPort> iports = new ArrayList<IPort>();
			iports.add(new IPort("192.168.5.4", 7777));
			
			for(IPort iport : iports) {
				if(!ipQueue.contains(iport)) {
					es.execute(new CheckHandler(iport));
				}
			}
		}
	}
	
	
	class CheckHandler implements Runnable {
		IPort iport;
		CheckHandler(IPort iport) {
			this.iport = iport;
		}
		
		@Override
		public void run() {
			if(check(iport)) {
				ipQueue.add(iport);
			} else {
				fiports.add(iport);
			}
		}
		
	}
	
	//检测ip可用性，并放在一个先进后出集合中
	public boolean check(IPort iport) {
		//visit a website
		String url = "http://www.taobao.com";
		try {
			Jsoup.connect(url).timeout(21000).proxy(iport.getIp(), iport.getPort(),null).execute();
			logger.debug("{} can use", iport);
			return true;
		} catch(Exception e) {
		}
		logger.debug("{} cannot use", iport);
		return false;
	}
	
}
