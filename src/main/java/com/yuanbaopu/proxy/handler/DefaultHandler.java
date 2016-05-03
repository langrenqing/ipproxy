package com.yuanbaopu.proxy.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import javax.net.ssl.SSLHandshakeException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuanbaopu.ip.IPoolFactory;
import com.yuanbaopu.ip.IPort;
import com.yuanbaopu.util.HttpsUtil;

public class DefaultHandler extends HandlerWrapper {
	
	protected static final Logger LOG = LoggerFactory.getLogger(DefaultHandler.class);
	
	private String POST_METHOD = "POST";
	
	public DefaultHandler() {
		
	}
	
	@Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, 
    		HttpServletResponse response) throws IOException, ServletException {
        try	{
        	HttpsUtil.trustEveryone();
        	org.jsoup.Connection.Response resp = reqToResp(request);
        	r2r(resp, response);
        	if(resp.dataStream() != null) {
        		sendConnectResponse(request, response, resp.dataStream(), HttpServletResponse.SC_OK);
        	} else {
        		byte[] html = resp.bodyAsBytes();
        		sendConnectResponse(request, response, html, HttpServletResponse.SC_OK);
        	}
            LOG.debug("handle success url = {} " , request.getRequestURL().toString());
        } catch (IOException x) {
        	LOG.warn("handle exception,url =" + request.getRequestURL().toString(), x);
        	int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        	if(x instanceof HttpStatusException) {
        		status = ((HttpStatusException) x).getStatusCode();
        	}
        	sendConnectResponse(request, response, x.getMessage().getBytes(), status);
        	
        	
        	if(x instanceof SSLHandshakeException) { // 在实践中发现出现Received fatal alert: handshake_failure时，访问主域名后，再请求就不报错
        		IPort iport = IPoolFactory.getIPool().getAIP();
        		//TODO connect be dynamic.
        		Jsoup.connect("http://www.taobao.com").proxy(iport.getIp(), iport.getPort(), iport.getAuthString()).execute();
        	}
        }
    }
	
	private void sendConnectResponse(HttpServletRequest request, HttpServletResponse response, byte[] msg, int statusCode) {
        try {
            response.setStatus(statusCode);
            if (statusCode != HttpServletResponse.SC_OK) {
                response.setHeader(HttpHeader.CONNECTION.asString(), HttpHeaderValue.CLOSE.asString());
            }
            response.getOutputStream().write(msg);
            response.getOutputStream().close();
            if (LOG.isDebugEnabled()) {
                LOG.debug("CONNECT response sent {} {}", request.getProtocol(), response.getStatus());
            }
        }
        catch (IOException x) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not send CONNECT response", x);
            }
        }
    }
	
	private void sendConnectResponse(HttpServletRequest request, HttpServletResponse response, InputStream is, int statusCode) {
        try {
            response.setStatus(statusCode);
            if (statusCode != HttpServletResponse.SC_OK) {
                response.setHeader(HttpHeader.CONNECTION.asString(), HttpHeaderValue.CLOSE.asString());
            }
            byte[] bytes = new byte[1024];
            while(is.read(bytes) != -1) {
            	response.getOutputStream().write(bytes);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("CONNECT response sent {} {}", request.getProtocol(), response.getStatus());
            }
        }
        catch (IOException x) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not send CONNECT response", x);
            }
        } finally {
        	try {
				response.getOutputStream().close();
			} catch (IOException e1) {
			}
        	try {
				is.close();
			} catch (IOException e) {
			}
        }
    }
	
	private org.jsoup.Connection.Response reqToResp(HttpServletRequest request) throws IOException {
		Connection conn = null;
		String url = request.getRequestURL().toString();
		if(POST_METHOD.equals(request.getMethod().toUpperCase())) {
			conn = HttpConnection.connect(url).method(Method.POST);
		} else {
			conn = HttpConnection.connect(url);
		}
		//cookie
		if(request.getCookies() != null)
		for(Cookie cookie : request.getCookies()) {
			conn.cookie(cookie.getName(), cookie.getValue());
		}
		
		//data
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			if(entry.getValue().length == 1) {
//				LOG.debug("args {} = {}", entry.getKey(),entry.getValue()[0]);
				conn.data(entry.getKey(), entry.getValue()[0]);
			} else if(entry.getValue().length > 1) {
				//warn
			} else {
				//ignore
			}
		}
		//proxy
		IPort iport = IPoolFactory.getIPool().getAIP();
		conn.timeout(5000).proxy(iport.getIp(), iport.getPort(), iport.getAuthString());
		return conn.execute();
	}
	
	private void r2r(org.jsoup.Connection.Response cr,HttpServletResponse hr) {
		hr.setStatus(cr.statusCode());
		for(Entry<String, String> entry : cr.headers().entrySet()) {
			if(HttpConnection.CONTENT_ENCODING.equals(entry.getKey())) {
				continue;
			}
			//System.out.println(entry.getKey() + "--header--" + entry.getValue());
			hr.addHeader(entry.getKey(), entry.getValue());
		}
		for(Entry<String, String> entry : cr.cookies().entrySet()) {
			//System.out.println(entry.getKey() + "--cookies--" + entry.getValue());
			hr.addCookie(new Cookie(entry.getKey(), entry.getValue()));
		}
		hr.setContentType(cr.contentType());
		hr.setCharacterEncoding(cr.charset());
	}
	
}
