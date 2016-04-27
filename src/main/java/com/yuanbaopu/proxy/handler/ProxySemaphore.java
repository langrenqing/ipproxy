package com.yuanbaopu.proxy.handler;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.utils.ZKPaths;

import com.yuanbaopu.proxy.helper.ZkHelper;

public interface ProxySemaphore {
	
	public AcquireResult acquire(long timeout, TimeUnit unit)
	        throws Exception;
	
	public void release(AcquireResult ar);
	
	public int getQueueLength();
	
}

class JDKSemaphore implements ProxySemaphore {
	
	Semaphore semaphore 				 = null;
	
	public JDKSemaphore(int limit) {
		semaphore = new Semaphore(limit); //机器数目
	}
	
	@Override
	public AcquireResult acquire(long timeout, TimeUnit unit) throws Exception {
		boolean result = semaphore.tryAcquire(timeout, unit);
		return new JDKAcquireResult(result);
	}

	@Override
	public void release(AcquireResult ar) {
		semaphore.release();
	}

	@Override
	public int getQueueLength() {
		return semaphore.getQueueLength();
	}
	
}

class ZKSemaphore implements ProxySemaphore {
	InterProcessSemaphoreV2 semaphore = null;
	
	ZKSemaphore(int limit) {
		semaphore = new InterProcessSemaphoreV2(ZkHelper.newClient(), ZKPaths.makePath(ZkHelper.getNamespace(),"semaphore"), limit);
	}

	@Override
	public AcquireResult acquire(long timeout, TimeUnit unit) throws Exception {
		Lease lease = semaphore.acquire(timeout, unit);
		return new ZKAcquireResult(lease);
	}

	@Override
	public void release(AcquireResult ar) {
		semaphore.returnLease((Lease)ar.getResult());
	}

	@Override
	public int getQueueLength() {
		try {
			return semaphore.getParticipantNodes().size();
		} catch (Exception e) {
		}
		return 0;
	}
	
}
