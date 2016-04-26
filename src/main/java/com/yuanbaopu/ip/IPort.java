package com.yuanbaopu.ip;

import com.yuanbaopu.proxy.auth.AuthProvider;
import com.yuanbaopu.proxy.auth.BlankAuthProvider;

public class IPort {

	AuthProvider auth = BlankAuthProvider.BLANKAUTHP;
	int 		 port = -1;
	String 		 ip;
	
	public IPort(String ip) {
		this.ip = ip;
	}
	
	public IPort(String ip, int port) {
		this(ip);
		this.port = port;
	}
	
	public IPort(String ip, int port,AuthProvider auth) {
		this(ip, port);
		this.auth = auth;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPort other = (IPort) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IPort [ip=" + ip + ", port=" + port + "]";
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getAuthString() {
		return auth.getAuth();
	}

	public AuthProvider getAuth() {
		return auth;
	}

	public void setAuth(AuthProvider auth) {
		this.auth = auth;
	}
	
}
