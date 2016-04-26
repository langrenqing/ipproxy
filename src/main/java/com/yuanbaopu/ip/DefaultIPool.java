package com.yuanbaopu.ip;

import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultIPool implements IPool {
	//可用
	protected Set<IPort> iports = new LinkedHashSet<IPort>();

	@Override
	public IPort getAIP() {
		return iports.iterator().next();
	}
	
	public void addIP(IPort ipport) {
		iports.add(ipport);
	}
}
