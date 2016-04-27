package com.yuanbaopu.proxy.handler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimitHandler extends HandlerWrapper {

	static int  DEFAULT_SLEEP            = 10;
	static int  MAX_WAIT                 = 10;
	protected static final Logger LOG    = LoggerFactory.getLogger(LimitHandler.class);
	ProxySemaphore semaphore 		     = null;
	
	public LimitHandler(int limit) {
		semaphore = SemaphoreFactory.createSemaphore(limit); //机器数目
	}
	
	
	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if(semaphore.getQueueLength() < MAX_WAIT) { // 竞争锁
			try {
				AcquireResult result = semaphore.acquire(DEFAULT_SLEEP, TimeUnit.SECONDS);
				if(result.goted()) {
					try {
						super.handle(target, baseRequest, request, response);
					} finally {
						semaphore.release(result);
					}
				} else { // 竞争失败
					sendConnectResponse(request, response, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				}
			} catch (Exception e1) {
				sendConnectResponse(request, response, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			} finally {
			}
		} else { // 不竞争，直接失败
			sendConnectResponse(request, response, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		int num = semaphore.getQueueLength();
		LOG.debug("connections = {} " , num);
		if(response.getStatus() == 421) {
			LOG.warn("too many connection.connections = {}" , num);
		}
	}
	
	private void sendConnectResponse(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        try {
            response.setStatus(statusCode);
            if (statusCode != HttpServletResponse.SC_OK)
                response.setHeader(HttpHeader.CONNECTION.asString(), HttpHeaderValue.CLOSE.asString());
            response.getOutputStream().close();
            if (LOG.isDebugEnabled())
                LOG.debug("CONNECT response sent {} {}", request.getProtocol(), response.getStatus());
        } catch (IOException x) {
            if (LOG.isDebugEnabled())
                LOG.debug("Could not send CONNECT response", x);
        }
    }
	
}
