package com.yuanbaopu.proxy.handler;

import org.apache.curator.framework.recipes.locks.Lease;

interface AcquireResult {
	boolean goted();
	Object getResult();
}

class JDKAcquireResult implements AcquireResult {
	boolean result;
	public JDKAcquireResult(boolean result) {
		this.result = result;
	}
	public boolean goted() {
		return result;
	}
	@Override
	public Object getResult() {
		return result;
	}
	
}

class ZKAcquireResult implements AcquireResult {
	Lease result;
	public ZKAcquireResult(Lease lease) {
		this.result = lease;
	}
	public boolean goted() {
		return result == null;
	}
	@Override
	public Object getResult() {
		return result;
	}
}
