package com.yuanbaopu.proxy.auth;

public class BlankAuthProvider implements AuthProvider {

	public final static AuthProvider BLANKAUTHP = new BlankAuthProvider();
	
	private BlankAuthProvider() {
		
	};
	
	@Override
	public String getAuth() {
		return null;
	}

	
}
